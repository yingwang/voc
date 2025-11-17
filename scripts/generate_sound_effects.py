#!/usr/bin/env python3
"""
Generate simple sound effects for the quiz app.
Creates OGG format audio files for correct and incorrect answers.
"""

import numpy as np
import wave
import subprocess
import os

def generate_sine_wave(frequency, duration, sample_rate=44100, amplitude=0.3):
    """Generate a sine wave."""
    t = np.linspace(0, duration, int(sample_rate * duration))
    wave = amplitude * np.sin(2 * np.pi * frequency * t)
    return wave

def apply_envelope(wave, attack=0.01, decay=0.1, sustain=0.7, release=0.2):
    """Apply ADSR envelope to the wave."""
    total_samples = len(wave)
    attack_samples = int(len(wave) * attack)
    decay_samples = int(len(wave) * decay)
    release_samples = int(len(wave) * release)
    sustain_samples = total_samples - attack_samples - decay_samples - release_samples

    envelope = np.ones(total_samples)

    # Attack
    envelope[:attack_samples] = np.linspace(0, 1, attack_samples)

    # Decay
    envelope[attack_samples:attack_samples + decay_samples] = \
        np.linspace(1, sustain, decay_samples)

    # Sustain
    envelope[attack_samples + decay_samples:attack_samples + decay_samples + sustain_samples] = sustain

    # Release
    envelope[-release_samples:] = np.linspace(sustain, 0, release_samples)

    return wave * envelope

def save_wav(filename, audio_data, sample_rate=44100):
    """Save audio data as WAV file."""
    # Normalize to 16-bit range
    audio_data = np.int16(audio_data * 32767)

    with wave.open(filename, 'w') as wav_file:
        wav_file.setnchannels(1)  # Mono
        wav_file.setsampwidth(2)  # 16-bit
        wav_file.setframerate(sample_rate)
        wav_file.writeframes(audio_data.tobytes())

def convert_to_ogg(wav_file, ogg_file):
    """Convert WAV to OGG using ffmpeg."""
    try:
        subprocess.run(
            ['ffmpeg', '-i', wav_file, '-c:a', 'libvorbis', '-q:a', '4', ogg_file, '-y'],
            check=True,
            capture_output=True
        )
        print(f"Created: {ogg_file}")
        os.remove(wav_file)
    except subprocess.CalledProcessError:
        print(f"Warning: Could not convert to OGG. Keeping WAV file: {wav_file}")
    except FileNotFoundError:
        print("Warning: ffmpeg not found. Keeping WAV files instead of OGG.")

def generate_correct_sound(output_path):
    """Generate a pleasant 'correct answer' sound - ascending notes."""
    sample_rate = 44100

    # Three ascending notes
    note1 = generate_sine_wave(523.25, 0.15, sample_rate)  # C5
    note2 = generate_sine_wave(659.25, 0.15, sample_rate)  # E5
    note3 = generate_sine_wave(783.99, 0.25, sample_rate)  # G5

    # Apply envelope
    note1 = apply_envelope(note1, attack=0.05, decay=0.2, sustain=0.6, release=0.15)
    note2 = apply_envelope(note2, attack=0.05, decay=0.2, sustain=0.6, release=0.15)
    note3 = apply_envelope(note3, attack=0.05, decay=0.2, sustain=0.5, release=0.25)

    # Add some silence between notes
    silence = np.zeros(int(sample_rate * 0.05))

    # Combine
    audio = np.concatenate([note1, silence, note2, silence, note3])

    # Save
    wav_path = output_path.replace('.ogg', '.wav')
    save_wav(wav_path, audio, sample_rate)
    convert_to_ogg(wav_path, output_path)

def generate_incorrect_sound(output_path):
    """Generate a gentle 'incorrect answer' sound - descending tone."""
    sample_rate = 44100

    # Two descending notes with slight dissonance
    note1 = generate_sine_wave(392.00, 0.20, sample_rate, amplitude=0.25)  # G4
    note2 = generate_sine_wave(293.66, 0.30, sample_rate, amplitude=0.25)  # D4

    # Apply envelope
    note1 = apply_envelope(note1, attack=0.1, decay=0.3, sustain=0.5, release=0.1)
    note2 = apply_envelope(note2, attack=0.1, decay=0.2, sustain=0.5, release=0.2)

    # Small overlap
    silence = np.zeros(int(sample_rate * 0.05))

    # Combine
    audio = np.concatenate([note1, silence, note2])

    # Save
    wav_path = output_path.replace('.ogg', '.wav')
    save_wav(wav_path, audio, sample_rate)
    convert_to_ogg(wav_path, output_path)

if __name__ == '__main__':
    output_dir = '/home/user/voc/app/src/main/res/raw'

    print("Generating sound effects...")

    # Generate correct answer sound
    correct_path = os.path.join(output_dir, 'sound_correct.ogg')
    generate_correct_sound(correct_path)

    # Generate incorrect answer sound
    incorrect_path = os.path.join(output_dir, 'sound_incorrect.ogg')
    generate_incorrect_sound(incorrect_path)

    print("Sound effects generated successfully!")

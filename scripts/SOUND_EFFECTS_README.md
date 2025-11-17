# Sound Effects Documentation

## Overview

This document describes the audio feedback system implemented in the Swedish Vocabulary Quiz app.

## Features

### 1. Auto-play Word Pronunciation
- **When**: Automatically when a new question is displayed
- **Delay**: 300ms after question loads (to avoid overwhelming the user)
- **Technology**: Android TextToSpeech (TTS) with Swedish locale
- **Manual Control**: Users can also click the pronunciation button to hear the word again

### 2. Answer Feedback Sounds

#### Correct Answer Sound
- **File**: `app/src/main/res/raw/sound_correct.wav`
- **Description**: Pleasant ascending melody (C5 → E5 → G5)
- **Duration**: ~0.55 seconds
- **Purpose**: Reward and encourage the learner

#### Incorrect Answer Sound
- **File**: `app/src/main/res/raw/sound_incorrect.wav`
- **Description**: Gentle descending tones (G4 → D4)
- **Duration**: ~0.55 seconds
- **Purpose**: Provide non-punitive feedback that an answer was incorrect

## Implementation Details

### AudioPlayer Class
Located at: `app/src/main/java/com/swedishvocab/utils/AudioPlayer.kt`

**Key Methods**:
- `speak()`: Uses TextToSpeech for Swedish word pronunciation
- `playCorrectSound()`: Plays the correct answer sound effect
- `playIncorrectSound()`: Plays the incorrect answer sound effect
- `release()`: Properly cleans up both TTS and MediaPlayer resources

**Technologies**:
- **TextToSpeech**: For Swedish word pronunciation
- **MediaPlayer**: For sound effects (correct/incorrect)

### QuizActivity Integration
Located at: `app/src/main/java/com/swedishvocab/QuizActivity.kt`

**Modifications**:
1. **displayCurrentQuestion()** (line ~137-143):
   - Auto-plays pronunciation with 300ms delay
   - Only plays if TTS is ready

2. **handleAnswerSelection()** (line ~185-190):
   - Plays correct sound for right answers
   - Plays incorrect sound for wrong answers
   - Sound plays immediately after selection

## Sound Generation

The sound effects were generated using the Python script:
`scripts/generate_sound_effects.py`

**Process**:
1. Generate sine waves at specific frequencies
2. Apply ADSR envelope for natural sound
3. Export as WAV format (Android compatible)

**Dependencies**:
- Python 3
- NumPy (for waveform generation)

**To regenerate sounds**:
```bash
python3 scripts/generate_sound_effects.py
```

## User Experience Flow

1. **Question Loads** → Word pronunciation plays automatically (300ms delay)
2. **User Answers** → Correct/incorrect sound plays immediately
3. **Next Question** → After 1500ms delay, next question loads and cycle repeats

## Benefits

- **Auditory Learning**: Reinforces correct Swedish pronunciation
- **Immediate Feedback**: Sound effects provide instant response to answers
- **Positive Reinforcement**: Pleasant sounds encourage continued learning
- **Non-punitive**: Incorrect sound is gentle, not discouraging
- **Accessibility**: Audio feedback helps users with different learning styles

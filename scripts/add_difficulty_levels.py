#!/usr/bin/env python3
"""
Script to add difficulty levels to dictionary words based on Swedish word frequency list.
Uses the frequency ranking from hermitdave/FrequencyWords to categorize words.
"""

import json
import sys
from typing import Dict, List, Tuple

# Difficulty level thresholds based on frequency rank
DIFFICULTY_THRESHOLDS = {
    'beginner': 3000,      # Top 3,000 most common words
    'intermediate': 10000, # Top 10,000 most common words
    'advanced': 20000,     # Top 20,000 most common words
}

def load_frequency_list(filepath: str) -> Dict[str, int]:
    """
    Load the frequency word list and create a mapping of word -> rank.

    Args:
        filepath: Path to the sv_50k.txt file

    Returns:
        Dictionary mapping Swedish words to their frequency rank (1-based)
    """
    word_ranks = {}
    rank = 1

    with open(filepath, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()
            if not line:
                continue

            parts = line.split()
            if len(parts) >= 2:
                word = parts[0].lower()  # Normalize to lowercase
                word_ranks[word] = rank
                rank += 1

    print(f"Loaded {len(word_ranks)} words from frequency list")
    return word_ranks

def assign_difficulty(rank: int) -> str:
    """
    Assign difficulty level based on frequency rank.

    Args:
        rank: Frequency rank (1 = most common)

    Returns:
        Difficulty level: 'beginner', 'intermediate', 'advanced', or 'expert'
    """
    if rank <= DIFFICULTY_THRESHOLDS['beginner']:
        return 'beginner'
    elif rank <= DIFFICULTY_THRESHOLDS['intermediate']:
        return 'intermediate'
    elif rank <= DIFFICULTY_THRESHOLDS['advanced']:
        return 'advanced'
    else:
        return 'expert'  # Beyond top 20k

def process_dictionary(dict_path: str, freq_path: str, output_path: str):
    """
    Process dictionary to add difficulty levels and sort by frequency.

    Args:
        dict_path: Path to dictionary.json
        freq_path: Path to frequency word list
        output_path: Path to save updated dictionary
    """
    # Load frequency rankings
    print("Loading frequency list...")
    word_ranks = load_frequency_list(freq_path)

    # Load dictionary
    print(f"Loading dictionary from {dict_path}...")
    with open(dict_path, 'r', encoding='utf-8') as f:
        dictionary = json.load(f)

    words = dictionary.get('words', [])
    print(f"Processing {len(words)} words...")

    # Statistics
    stats = {
        'beginner': 0,
        'intermediate': 0,
        'advanced': 0,
        'expert': 0,
        'not_found': 0
    }

    # Process each word
    for word_entry in words:
        swedish_word = word_entry['swedish'].lower()

        # Try to find the word in frequency list
        rank = word_ranks.get(swedish_word)

        if rank is not None:
            difficulty = assign_difficulty(rank)
            word_entry['difficulty'] = difficulty
            word_entry['frequencyRank'] = rank
            stats[difficulty] += 1
        else:
            # Word not found in frequency list - mark as expert (rare)
            word_entry['difficulty'] = 'expert'
            word_entry['frequencyRank'] = 999999  # Assign very high rank
            stats['not_found'] += 1

    # Sort words by frequency rank (most common first)
    print("Sorting words by frequency...")
    words.sort(key=lambda w: w.get('frequencyRank', 999999))

    # Update dictionary
    dictionary['words'] = words

    # Save updated dictionary
    print(f"Saving updated dictionary to {output_path}...")
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(dictionary, f, ensure_ascii=False, indent=2)

    # Print statistics
    print("\n=== Statistics ===")
    print(f"Beginner (1-{DIFFICULTY_THRESHOLDS['beginner']}): {stats['beginner']} words")
    print(f"Intermediate ({DIFFICULTY_THRESHOLDS['beginner']+1}-{DIFFICULTY_THRESHOLDS['intermediate']}): {stats['intermediate']} words")
    print(f"Advanced ({DIFFICULTY_THRESHOLDS['intermediate']+1}-{DIFFICULTY_THRESHOLDS['advanced']}): {stats['advanced']} words")
    print(f"Expert (>{DIFFICULTY_THRESHOLDS['advanced']}): {stats['expert']} words")
    print(f"Not found in frequency list: {stats['not_found']} words")
    print(f"\nTotal words: {len(words)}")
    print(f"\nDictionary updated successfully!")

if __name__ == '__main__':
    # Paths
    dict_path = '/home/user/voc/app/src/main/res/raw/dictionary.json'
    freq_path = '/tmp/sv_50k.txt'
    output_path = dict_path  # Overwrite original

    process_dictionary(dict_path, freq_path, output_path)

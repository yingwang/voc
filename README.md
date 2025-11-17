# Swedish Vocab - Learn Swedish Vocabulary

A modern Android app designed to help you learn Swedish vocabulary through interactive quizzes with native pronunciation.

## Features

- **36,000+ Swedish Words**: Complete Folkets Lexikon dictionary with comprehensive vocabulary
- **Native Audio Pronunciation**: Stream Swedish pronunciation for 18,500+ words from Folkets Lexikon
- **Phonetic Transcriptions**: See IPA pronunciation guides for proper learning
- **Fully Randomized Quizzes**: Every question and answer set is dynamically generated - never the same quiz twice
- **Multiple-Choice Quiz Mode**: 30 questions per game with 4 randomized answer options
- **Smart Answer Generation**: Incorrect options are randomly selected from the entire vocabulary
- **Points & High Scores**: Track your progress and beat your high score
- **Modern Material Design UI**: Clean, light, and tech-inspired interface
- **Instant Feedback**: See correct answers immediately with color-coded visual feedback
- **Progress Tracking**: View your performance with percentage scores and encouraging messages

## Dictionary Categories

The app includes vocabulary from the following categories:
- Greetings & Basic Phrases
- Numbers (1-1000)
- Colors
- Food & Drinks
- Animals
- Family & People
- Home & Furniture
- Days, Months & Time
- Transportation
- Places
- Activities & Verbs
- Adjectives & Adverbs
- Body Parts
- Clothing
- Weather
- Common Phrases

## How to Play

1. Tap "Start Quiz" on the home screen
2. Read the Swedish word or phrase displayed
3. **Tap the 🔊 Listen button** to hear native Swedish pronunciation (when available)
4. View the phonetic transcription to learn proper pronunciation
5. Select the correct English translation from 4 randomized options
6. Get instant feedback (green for correct, red for incorrect)
7. Complete all 30 questions from randomly selected vocabulary
8. View your score and try to beat your high score!

## Randomization Features

The app ensures maximum variety and learning effectiveness through comprehensive randomization:

- **Question Selection**: 30 words are randomly selected from 36,000+ entries for each game
- **Answer Options**: For each question, 3 incorrect answers are randomly chosen from the entire vocabulary
- **Option Order**: All 4 answer choices (1 correct + 3 incorrect) are shuffled randomly
- **No Hardcoded Questions**: Everything is dynamically generated - play infinite unique quizzes

This means you'll never encounter the same quiz twice, ensuring continuous learning and variety!

## Technical Details

- **Language**: Kotlin
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: Modern Android with ViewBinding and MediaPlayer
- **Data Storage**: SharedPreferences for score persistence
- **Dictionary Source**: Folkets Lexikon (36,148 words)
- **Dictionary Format**: JSON file in raw resources
- **Audio Streaming**: MediaPlayer with internet audio streaming
- **License**: CC-BY-SA 2.5 (Folkets Lexikon dictionary)

## Building the App

### Prerequisites

- Android Studio (latest version recommended)
- JDK 8 or higher
- Android SDK with API 34

### Build Instructions

1. Clone or download this repository
2. Open the project in Android Studio
3. Wait for Gradle sync to complete
4. Click "Run" or use `./gradlew assembleDebug`

### Building from Command Line

```bash
# On Linux/Mac
./gradlew assembleDebug

# On Windows
gradlew.bat assembleDebug
```

The APK will be generated in `app/build/outputs/apk/debug/`

## Expanding the Dictionary

To add more words, edit `app/src/main/res/raw/dictionary.json`:

```json
{
  "words": [
    {
      "swedish": "your_swedish_word",
      "english": "your_english_translation",
      "category": "your_category"
    }
  ]
}
```

## Open Source Dictionary

This app uses a curated Swedish-English dictionary. You can expand it with data from:
- **Lexin Swedish-English Dictionary** (CC BY 4.0) - 22,000+ entries
- **Folkets Lexikon** - Community-driven dictionary
- Available at: https://researchdata.se/en/catalogue/dataset/ext0299-1

## Screenshots

The app features:
- **Home Screen**: Displays high score and start quiz button
- **Quiz Screen**: Shows Swedish word with 4 English options
- **Results Screen**: Displays final score, percentage, and performance message

## Performance Messages

Based on your score:
- 90%+: "Outstanding! You're mastering Swedish! 🌟"
- 75-89%: "Excellent work! Keep practicing! 💪"
- 60-74%: "Good job! You're making great progress! 👍"
- 50-59%: "Not bad! Keep studying to improve! 📚"
- Below 50%: "Keep practicing! You'll get better! 🎯"

## License

This project is open source. The dictionary content can be expanded using CC-licensed Swedish-English dictionaries.

## Contributing

Feel free to:
- Add more vocabulary words
- Improve the UI/UX
- Add new features (e.g., difficulty levels, timed mode, categories selection)
- Fix bugs

## Future Enhancements

Potential features for future versions:
- Difficulty levels (easy, medium, hard)
- Category-specific quizzes
- Timed challenge mode
- Voice pronunciation
- Spaced repetition system
- Statistics and learning analytics
- Dark mode support
- Word favorites and review lists

---

**Happy Learning! Lycka till!** 🇸🇪

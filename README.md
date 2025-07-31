# PillCare - Smart Pill Reminder App

PillCare is a comprehensive Android application designed to help users manage their medication schedule efficiently and ensure timely intake of prescribed pills. Built with Java and modern Android development practices, it provides a user-friendly interface for medication management with smart reminders.

## 🔑 Key Features

### Core Functionality
- **Medication Management**: Add, edit, and delete medications with detailed information
- **Smart Reminders**: Customizable push notifications and alarm-based alerts
- **Daily Pill Tracker**: Track which medications were taken or missed throughout the day
- **Multiple Time Slots**: Set multiple reminder times for each medication
- **Flexible Scheduling**: Support for different frequencies and date ranges

### User Experience
- **Material Design UI**: Clean and intuitive interface following Material Design guidelines
- **Navigation Drawer**: Easy navigation between different sections of the app
- **Color-Coded Medications**: Visual differentiation with customizable colors
- **Status Indicators**: Visual feedback for medication status (taken, missed, pending, overdue)

### Data Management
- **Local Storage**: Secure local storage using Room Database (SQLite)
- **Data Persistence**: All medication data stored locally on the device
- **Backup Support**: Android's built-in backup and restore functionality

## 🏗️ Architecture & Technologies

### Core Technologies
- **Language**: Java
- **Platform**: Android (API 24+)
- **IDE**: Android Studio
- **Build System**: Gradle

### Architecture Components
- **Database**: Room Database with SQLite
- **UI**: XML-based layouts with Material Design components
- **Architecture Pattern**: MVVM (Model-View-ViewModel)
- **Background Tasks**: AlarmManager for precise scheduling
- **Notifications**: Android Notification API with channels

### Dependencies
- **Room**: Database abstraction layer
- **Material Design**: Modern UI components
- **Lifecycle Components**: ViewModel and LiveData
- **WorkManager**: Background task scheduling
- **Gson**: JSON serialization for complex data types

## 📱 App Structure

### Main Components

#### Activities
- `MainActivity`: Main screen with medication list and navigation
- `AddMedicationActivity`: Form for adding new medications
- `EditMedicationActivity`: Edit existing medication details
- `MedicationDetailsActivity`: View detailed medication information
- `DailyTrackerActivity`: Daily overview and statistics

#### Database Layer
- `Medication`: Entity representing medication data
- `MedicationLog`: Entity for tracking medication intake
- `MedicationDao`: Data access object for medications
- `MedicationLogDao`: Data access object for logs
- `PillCareDatabase`: Room database implementation

#### Services & Utilities
- `AlarmReceiver`: Handles medication reminder alarms
- `NotificationActionReceiver`: Processes notification actions
- `NotificationUtils`: Utility for scheduling and managing notifications
- `DateTimeUtils`: Date and time formatting utilities

### Database Schema

#### Medications Table
- `id`: Primary key (auto-generated)
- `name`: Medication name
- `description`: Optional description
- `dosage`: Dosage information (e.g., "10mg", "1 tablet")
- `frequency`: Number of times per day
- `times`: JSON array of reminder times
- `startDate`: Start date for medication
- `endDate`: End date for medication
- `isActive`: Whether medication is currently active
- `instructions`: Special instructions
- `color`: UI color for visual identification
- `reminderEnabled`: Whether reminders are enabled
- `reminderMinutesBefore`: Minutes before scheduled time to remind

#### Medication Logs Table
- `id`: Primary key (auto-generated)
- `medicationId`: Foreign key to medications table
- `scheduledTime`: When medication was scheduled to be taken
- `takenTime`: When medication was actually taken (if taken)
- `wasTaken`: Boolean indicating if medication was taken
- `wasMissed`: Boolean indicating if medication was missed
- `notes`: Optional notes about the medication intake

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK with API level 24 or higher
- Java Development Kit (JDK) 8 or later

### Installation
1. Clone or download the project files
2. Open the project in Android Studio
3. Wait for Gradle sync to complete
4. Connect an Android device or start an emulator
5. Run the app using the "Run" button or `Shift + F10`

### Building the Project
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test
```

## 📋 Usage Guide

### Adding a New Medication
1. Tap the "+" floating action button on the main screen
2. Fill in the medication details:
   - Name (required)
   - Dosage (required)
   - Description (optional)
   - Frequency (times per day)
   - Reminder times (at least one required)
   - Start and end dates
   - Special instructions (optional)
3. Configure reminder settings
4. Tap "Save" to add the medication

### Managing Medications
- **View Details**: Tap on any medication card to view full details
- **Edit**: Use the edit button in medication details or the menu
- **Delete**: Use the delete button in medication details
- **Toggle Active/Inactive**: Use the switch in medication details
- **Enable/Disable Reminders**: Use the reminder switch

### Daily Tracking
- Access the daily tracker from the navigation drawer
- View statistics: taken, missed, and completion rate
- Mark medications as taken directly from the list
- Track your medication adherence over time

### Notifications
- Receive notifications at scheduled times
- Use "Taken" action to mark medication as taken
- Use "Snooze" action to postpone reminder by 10 minutes
- Notifications include medication name and dosage

## 🔧 Customization

### Adding New Features
The app is designed with extensibility in mind:

1. **New Activities**: Follow the existing pattern in the `activities` package
2. **Database Changes**: Update entities in the `models` package and increment database version
3. **New Notification Types**: Extend the `NotificationUtils` class
4. **UI Themes**: Modify colors and styles in the `res/values` directory

### Configuration Options
- **Notification Channels**: Modify in `AlarmReceiver.createNotificationChannel()`
- **Database Name**: Change in `PillCareDatabase.getDatabase()`
- **Default Colors**: Update in `res/values/colors.xml`
- **App Theme**: Modify in `res/values/styles.xml`

## 🎨 UI/UX Design

### Design Principles
- **Material Design**: Follows Google's Material Design guidelines
- **Accessibility**: Proper content descriptions and touch targets
- **Responsive**: Adapts to different screen sizes and orientations
- **Intuitive Navigation**: Clear navigation patterns and visual hierarchy

### Color Scheme
- **Primary**: Blue (#2196F3) - Trust and reliability
- **Success**: Green (#4CAF50) - Medication taken
- **Error**: Red (#F44336) - Medication missed
- **Warning**: Orange (#FF9800) - Pending/overdue medications

## 🔒 Privacy & Security

### Data Protection
- **Local Storage**: All data stored locally on the device
- **No Network Access**: App doesn't transmit personal data
- **Secure Database**: Room database with built-in security features
- **Backup Control**: Users control backup and restore through Android settings

### Permissions
- `SCHEDULE_EXACT_ALARM`: For precise medication reminders
- `WAKE_LOCK`: To ensure notifications work when device is sleeping
- `VIBRATE`: For notification vibration
- `POST_NOTIFICATIONS`: To display reminder notifications
- `RECEIVE_BOOT_COMPLETED`: To restore alarms after device restart

## 🧪 Testing

### Testing Strategy
- **Unit Tests**: Test business logic and utility functions
- **Integration Tests**: Test database operations and ViewModels
- **UI Tests**: Test user interactions and navigation flows

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest
```

## 🚀 Future Enhancements

### Planned Features
- **Multiple User Profiles**: Support for family medication management
- **Medication History**: Detailed history and analytics
- **Export/Import**: Backup and restore medication data
- **Widget Support**: Home screen widget for quick access
- **Integration**: Healthcare provider integration capabilities
- **Advanced Scheduling**: Support for complex medication schedules



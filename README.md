# Dog Dating App

The Dog Dating App is an Android application that allows dog owners to create profiles for their furry friends and help them connect with other local dogs for friendship, playdates or even romance.

## Screenshots

## Features

**Authentication**
- Email/Password login
- Google Sign-In

**Dog Profile Setup**
- Upload photos
- Enter breed, size, age, temperament, activity level, and preferences

**Location Selection**
- Location detected when account is created

**Matching System**
- View dog profiles via `RecyclerView`
- Match / Reject buttons
- View full profiles on match

**Communication**
- Real-time chat
- Schedule playdates or romantic dates
- Invite multiple dogs

**Date Scheduling**
- Select time, location, and activity (e.g., dog park, shopping, dining)
- Notification invites & safety alerts
- Smart rules: Blocks unsafe times/locations

**Social Timeline**
- Share photos on profile
- View photos of matched or nearby dogs

**Relationship Milestones**
- Status update to "Dating" or "Married"
- Send announcements, wedding invites, gift lists
  
**Backend**
- Firebase Authentication & Realtime Database

## Tech Stack

| Component         | Technology                                |
|------------------|--------------------------------------------|
| Language          | Kotlin                                     |
| UI                | Material Design, ConstraintLayout          |
| Architecture      | MVVM with ViewModel & LiveData             |
| Auth              | Firebase Authentication, Google Sign-In   |
| Storage           | Firebase Realtime Database                |
| Notifications     | Android NotificationManager                |
| Chat              | Firebase Messaging        |
| Maps (Optional)   | Google Maps SDK            |
| Localization      | Android resource-based i18n                |

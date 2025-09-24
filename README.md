# NoteMinds 🧠📱

An AI-powered mobile learning application that transforms your notes into interactive summaries and quizzes using advanced machine learning technology.

## 📋 Table of Contents
- [Features](#features)
- [Screenshots](#screenshots)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Installation](#installation)
- [Usage](#usage)
- [AI Integration](#ai-integration)
- [Collaboration Features](#collaboration-features)
- [Performance & Security](#performance--security)
- [Contributing](#contributing)
- [Future Roadmap](#future-roadmap)
- [Contact](#contact)

## ✨ Features

### 🤖 AI-Powered Learning Tools
- **Smart Summaries**: Transform lengthy notes into concise, structured summaries using LLaMA 2
- **Intelligent Quizzes**: Generate context-aware questions from your study materials
- **< 3 Second Response Time**: Optimized AI processing for instant results

### 👥 Real-Time Collaboration
- **Group Chat**: Collaborate with peers in real-time
- **AI Moderation**: Intelligent chat monitoring for productive discussions
- **Offline Messaging**: Queue messages when offline, sync when connected

### 📤 Seamless Sharing
- **Email Integration**: Export summaries directly via email with pre-filled content
- **Multiple Formats**: Share as text, HTML, or prepare for PDF export
- **System Share Menu**: Native Android sharing experience

### 🎨 Enhanced User Experience
- **Material 3 Design**: Modern, adaptive theming with accessibility compliance
- **Lottie Animations**: Smooth loading states during AI processing
- **Single Activity Architecture**: Efficient navigation and performance

## 📱 Screenshots
<!-- Add your screenshots here -->
<img width="30%" alt="Screenshot_1748053863" src="https://github.com/user-attachments/assets/401399d1-d608-4748-b71e-1b1127127cc9" />
<img width="30%" alt="Screenshot_1748053868" src="https://github.com/user-attachments/assets/b2a55dd0-fac4-4bc4-9b8a-a3eb57e19573" />

*Main dashboard with note management*
<div align="center">
<img  width="30%" src="https://github.com/user-attachments/assets/587a8541-62a4-4501-b148-8d8b568d33a9" />
</div>
*AI-generated summary interface*
<div align="center">
<img width="30%" alt="Screenshot_1748062040" src="https://github.com/user-attachments/assets/c7901a9c-ebab-4b89-a009-f91af60b85be" />
<img width="30%" alt="Screenshot_1748062048" src="https://github.com/user-attachments/assets/b48f2582-b47e-4c0e-9eba-c4699ee2bcda" />
<img width="30%" alt="Screenshot_1748062096" src="https://github.com/user-attachments/assets/61c061bb-1e4f-486a-aec1-2a1b458eedab" />
<img width="30%" alt="Screenshot_1748062202" src="https://github.com/user-attachments/assets/c6c6a3f8-d12d-40e1-9c11-b96529ddb6ec" />
<img width="30%" alt="Screenshot_1748062232" src="https://github.com/user-attachments/assets/a5c3244d-61d6-4928-8899-dbfa5d074898" />
</div>

*Real-time chat collaboration*
<img width="30%" alt="Screenshot_1748062051" src="https://github.com/user-attachments/assets/8595ff52-4be7-49de-afdc-37b487d2d22f" />
<img width="30%" alt="Screenshot_1748062063" src="https://github.com/user-attachments/assets/4fd2407a-5f0f-4a33-a43d-e3f481aaa392" />
<img width="30%" alt="Screenshot_1748062067" src="https://github.com/user-attachments/assets/9f7fecd7-da88-4443-ba47-652dfdd698e9" />
<img width="30%" alt="Screenshot_1748062417" src="https://github.com/user-attachments/assets/bc9fe36f-5d99-49da-b0c9-3894fa47a7e3" />
<img width="30%" alt="Screenshot_1748062434" src="https://github.com/user-attachments/assets/c924def2-9050-42c7-a15f-663d03acdc2f" />


*Quiz generation from notes*
<img width="30%" alt="Screenshot_1748062240" src="https://github.com/user-attachments/assets/6afc8765-6025-4278-9609-2846893992ff" />
<img width="30%" alt="Screenshot_1748062344" src="https://github.com/user-attachments/assets/c3ed587b-9c71-43ea-8560-a25ab1a6cd96" />


## 🏗️ Architecture

NoteMinds follows the **MVVM (Model-View-ViewModel)** architectural pattern with clean code principles:
<img width="733" height="550" alt="image" src="https://github.com/user-attachments/assets/b116d7af-2155-4ab9-ae29-1974ddcbda9b" />

**AI Microservices Backend:**
<img width="940" height="378" alt="image" src="https://github.com/user-attachments/assets/4a31fbd7-b2a8-4abe-b52a-ce6a578787df" />


### System Architecture Diagram
<img width="940" height="509" alt="image" src="https://github.com/user-attachments/assets/4f453f4c-2116-47db-a582-e998248aabaa" />


## 🛠️ Technology Stack

### Frontend (Android)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose / XML
- **Architecture**: MVVM + Single Activity
- **Reactive Programming**: LiveData/Flow + Coroutines
- **Animations**: Lottie

### Backend & AI
- **AI Framework**: LLaMA 2 (Large Language Model)
- **API Server**: Flask (Python)
- **Response Time**: < 3 seconds optimized processing

### Database Strategy
- **Firebase Realtime Database**: Real-time chat synchronization
- **MongoDB Atlas**: AI-generated content and user analytics
- **Room Database**: Local caching for offline access

### Networking & Security
- **HTTP Client**: Retrofit + OkHttp
- **Authentication**: JWT (JSON Web Tokens)
- **API Architecture**: RESTful microservices

## 📦 Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+
- Kotlin 1.8+
- Internet connection for AI features

### Setup Steps

1. **Clone the repository**
```bash
git clone https://github.com/AMG786/NoteMinds---Mobile-App.git
cd NoteMinds---Mobile-App
```

2. **Configure Firebase**
   - Add your `google-services.json` file to the `app/` directory
   - Configure Firebase Realtime Database rules

3. **Set up MongoDB Atlas**
   - Configure connection string in `app/src/main/java/config/DatabaseConfig.kt`

4. **Build and run**
```bash
./gradlew build
./gradlew installDebug
```

## 🚀 Usage

### Getting Started
1. **Create Account**: Sign up with email or social login
2. **Add Notes**: Import or create study materials
3. **Generate AI Content**: Tap "Summarize" or "Create Quiz"
4. **Collaborate**: Join group chats or create study groups
5. **Share**: Export summaries via email or other apps

### AI Features Usage
- **Smart Summaries**: Select text → "Generate Summary" → Review & edit
- **Quiz Creation**: Choose notes → "Create Quiz" → Customize difficulty
- **Chat Integration**: Ask AI questions in group discussions

## 🤖 AI Integration

### LLaMA 2 Processing Pipeline
```
User Notes → Text Processing → LLaMA 2 Model → Structured Output → Mobile App
     ↓              ↓              ↓              ↓              ↓
  Raw Text    → Preprocessing → AI Analysis → JSON Response → UI Update
```

### API Endpoints
- `POST /api/summarize` - Generate note summaries
- `POST /api/quiz` - Create interactive quizzes
- `GET /api/chat` - AI chat responses

## 👥 Collaboration Features

- **Real-time Messaging**: Instant sync across devices
- **Group Management**: Create, join, and moderate study groups
- **AI Moderation**: Automatic content filtering and suggestions
- **Offline Support**: Message queuing with sync on reconnection

## 🔒 Performance & Security

### Performance Optimizations
- **Coroutines**: Non-blocking async operations
- **Flow**: Reactive data streams for UI updates
- **Caching**: Local storage with Room database
- **Lazy Loading**: Efficient memory management

### Security Measures
- **JWT Authentication**: Secure API access
- **Encrypted Storage**: Sensitive data protection
- **Network Security**: HTTPS/TLS encryption
- **Input Validation**: Sanitized user inputs

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow Kotlin coding conventions
- Write unit tests for new features
- Update documentation as needed
- Ensure Material Design compliance

## 🛣️ Future Roadmap

### Upcoming Features
- **📎 Enhanced Sharing**: PDF attachments and LMS integration (Moodle)
- **📊 Analytics Dashboard**: Track summary sharing and usage patterns
- **🎯 Improved AI**: Enhanced accuracy and personalization
- **🌐 Web Platform**: Cross-platform accessibility
- **📚 Study Planner**: Intelligent scheduling and reminders

### Version 2.0 Goals
- [ ] Offline AI processing
- [ ] Voice note integration
- [ ] Collaborative editing
- [ ] Advanced analytics
- [ ] Multi-language support

## 📞 Contact

**Developer**: Abdul Mueez  
**Student ID**: s223522835  
**Institution**: Deakin University  
**Unit**: SIT708 Mobile Application Development  

### Links
- 📱 **GitHub Repository**: [NoteMinds Mobile App](https://github.com/AMG786/NoteMinds---Mobile-App/tree/master)
- 🎥 **Demo Video**: [Presentation Link](https://deakin.au.panopto.com/Panopto/Pages/Viewer.aspx?id=30477b95-f7e6-44f3-97e4-b2e7004c934f)
- 📁 **Additional Resources**: [Backend Code & Slides](https://drive.google.com/drive/folders/1Ka1YcfKDpSXUo44Ueh0aydZXudXblDFZ?usp=sharing)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Deakin University SIT708 Course Team
- Tutor: Shiva Pokhrel
- LLaMA 2 AI Model by Meta
- Firebase & MongoDB for backend services
- Material Design team at Google

---

**⭐ If you find this project helpful, please give it a star!**

*Built with ❤️ using Kotlin and AI*

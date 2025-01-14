# CropSense ![cropsense_icon](https://github.com/user-attachments/assets/84e58dc6-7a40-46c4-a937-2d438fb0cbf9)

CropSense is an intelligent chatbot system that helps users analyze agricultural images and provides educational information about crops without the need for extensive research or literature review. The application combines real-time weather data and AI-powered analysis to deliver instant insights about crops and farming practices.

## Contributors
- [Jovan Mosurović](https://github.com/JovanMosurovic)
- [Filip Petrović](https://github.com/fili5rovic)
- [Veljko Zavišić](https://github.com/Vexa004)

## Key Features
- Image analysis of crops and fields for disease identification
- Quick access to agricultural knowledge through natural conversation
- Real-time weather data integration for informed decision-making
- Educational resources for farming beginners through interactive chat
- Image-based crop identification and health assessment
- Optional Weather Mode for location-based climate insights

## Technical Integration

### Weather Mode
The application includes an optional Weather Mode that, when enabled:
- Automatically detects user's current location
- Fetches real-time weather data for the specific location
- Provides temperature, precipitation, and other weather metrics
- Integrates weather data into farming recommendations
- Updates continuously while enabled

### Weather API
Integrates Open Meteo API for real-time weather data including temperature forecasts and precipitation predictions to provide context-aware farming recommendations.

### AI Integration
Currently uses Google's Gemini AI models for prototype testing:
- Gemini 1.0 Pro handles text analysis and agricultural knowledge queries
- Gemini 1.5 Flash processes images for crop and disease identification

Note: While the current version uses general-purpose Gemini models, the system is designed to accommodate specialized agricultural AI models in the future for improved accuracy and domain-specific analysis.

Important: Although using general-purpose AI models, CropSense is specifically configured to only respond to agriculture-related queries. The chatbot will not engage with questions or topics outside of farming and agriculture to maintain its focused educational and analytical purpose.

## Getting Started
1. Open CropSense application
2. Enable Weather Mode if desired
3. Upload an image or ask a farming-related question
4. Receive AI-powered analysis and recommendations
5. View location-specific weather data when Weather Mode is active

## User Interface

<img src="https://github.com/user-attachments/assets/1d1d14ca-46dc-4deb-8810-8ca6ea282784" width="70%" height="70%" alt="CropSense Interface">

## Notes
- Internet connection required for API functionality
- Supports image uploads for visual analysis
- No prior farming knowledge needed
- Real-time weather data integration
- Instant responses to agricultural queries
- Focused solely on agriculture-related topics and questions
- Location services required for Weather Mode functionality

## Support
For technical support or feature requests, please raise an issue in our GitHub repository.

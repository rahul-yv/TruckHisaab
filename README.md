<p align="center">
  <img src="https://raw.githubusercontent.com/rahul-yv/TruckHisaab/main/assets/logo.png" alt="TruckHisaab Logo" width="180">
</p>
<h1 align="center">🚛 TruckHisaab</h1>
<p align="center">
  <b>Indian Truck Owners Ka WhatsApp AI Assistant</b><br>
  <i>Trip logging, expense tracking, P&L reports — bas WhatsApp pe, Hindi mein, bilkul FREE</i>
</p>
<p align="center">
  <a href="https://wa.me/918879688678" target="_blank">
    <img src="https://img.shields.io/badge/WhatsApp-Chat%20Now-25D366?style=for-the-badge&logo=whatsapp&logoColor=white" alt="WhatsApp Chat">
  </a>
  <a href="https://truckhisaab.onrender.com" target="_blank">
    <img src="https://img.shields.io/badge/Status-LIVE-brightgreen?style=for-the-badge" alt="Status Live">
  </a>
  <img src="https://img.shields.io/badge/Users-50%2B-blue?style=for-the-badge" alt="50+ Users">
  <img src="https://img.shields.io/badge/Language-Hindi%2FHinglish-orange?style=for-the-badge" alt="Hindi Support">
</p>
📖 Kya Hai TruckHisaab?
India mein 95 lakh+ trucks hain aur 2.5 crore+ truck drivers/owners. Inme se 95% small operators hain (1-5 trucks). Aaj bhi ye log paper diary mein hisaab rakhte hain, expenses yaad karte hain, ya bilkul nahi karte.
Result: Har saal ₹15,000-40,000 ka nuksaan poor record keeping ki wajah se.
TruckHisaab = WhatsApp + Hindi + Simple + FREE
Koi app download nahi. Koi training nahi. Bas WhatsApp pe message karo, sab kuch track ho jayega.
✨ Features
Table
Feature	Kaise Use Karein	Example
📝 Trip Logging	Route + Cargo + Amount batao	"Mumbai se Pune, 12 ton cement, 28000"
💰 Expense Tracking	Koi bhi kharcha batao	"Diesel 4500 bhara"
📊 P&L Reports	Hisaab poochho	"Aaj kitna kamaya"
🔔 Daily Reminders	Auto 8 PM reminder	"Bhai aaj ka kharcha?"
📄 Monthly PDF	Auto P&L report	"Is mahine ka hisaab"
📅 Document Alerts	RC, Insurance, PUC expiry	"RC check karo"
🗣️ Voice Support	Bolke batao	Hindi voice messages
🌐 Multi-language	Hindi/English/Hinglish	Sab support hai
🚀 Live Demo
WhatsApp Bot
📱 Save & Chat: +91 88796 88678
Step 1: WhatsApp pe +91 88796 88678 save karo
Step 2: "Hi" bhejo
Step 3: Apna naam aur truck number batao
Step 4: Bas! Ab har trip, har kharcha track karo
Quick Commands
plain
"Hi"              → Onboarding
"Mumbai se Pune, 12 ton cement, 28000"  → Trip log
"Diesel 4500 bhara"  → Expense track
"Aaj kitna kamaya"   → P&L query
"Trip complete"      → Final hisaab
"Help" / "Madad"     → Command list
🏗️ Tech Stack
Table
Layer	Technology	Purpose	Cost
Backend	Node.js + Express	Server + API	FREE
Database	MongoDB Atlas	User/Trip/Expense data	FREE (512MB)
AI Brain	Google Gemini 2.0 Flash	Hindi/Hinglish NLP	FREE tier
Messaging	Meta WhatsApp Cloud API	WhatsApp bot	1000 conv FREE/month
Hosting	Render.com	Server deployment	FREE
App	React Native (Phase 2)	Mobile app	FREE (dev)
Analytics	Firebase	User tracking	FREE
💡 Key Insight: ₹2,000/month mein production-grade AI WhatsApp bot chal sakta hai. Yeh barrier to entry bahut low hai.
📊 Market Opportunity
Table
Metric	Number	Opportunity
Registered Trucks India	95 Lakh+	Target market
Truck Drivers & Owners	2.5 Crore+	Potential users
Industry Revenue	₹8.5 Lakh Crore	Massive market
Small Operators (1-5 trucks)	95% of all	Underserved segment
Using Digital Tools	< 5%	Huge gap = opportunity
Annual loss per trucker	₹15,000-40,000	Pain = willingness to pay
WhatsApp Users India	53 Crore	Free distribution
Hindi Speaking Truckers	70%+	Language advantage
TAM: 2.5 Crore | SAM: 50 Lakh | SOM: 2.5 Lakh (Year 3)
💰 Revenue Model
Freemium SaaS
Table
Plan	Price	Features
FREE	₹0	Trip logging, expense tracking, basic P&L
PRO	₹199/month	PDF reports, GST invoices, document reminders, voice
FLEET	₹999/month	Multi-truck, fleet dashboard, driver management
Marketplace (Future)
Insurance comparison (₹500-2,000/policy commission)
Load marketplace (2-3% per booking)
Working capital loans (1-2% referral)
FASTag recharge, diesel booking, spare parts
🎯 12-Month Roadmap
Table
Month	Users	Trips	Paid	MRR	Team
1	50	100	0	₹0	1 (Solo)
3	300	800	15	₹2,985	1
6	1,200	4,000	100	₹19,900	3
9	5,000	18,000	450	₹89,550	5
12	10,000	40,000	1,000	₹1,99,000	8
🛠️ Getting Started (Developers)
Prerequisites
Node.js v18+
MongoDB Atlas account
Meta WhatsApp Business API account
Google Gemini API key
Installation
bash
# Clone the repo
git clone https://github.com/rahul-yv/TruckHisaab.git
cd TruckHisaab

# Install dependencies
npm install

# Environment variables
cp .env.example .env
# Edit .env with your API keys

# Start development server
npm run dev

# Webhook setup (for WhatsApp)
ngrok http 3000
Environment Variables
env
PORT=3000
MONGODB_URI=your_mongodb_uri
WHATSAPP_TOKEN=your_meta_token
WHATSAPP_PHONE_ID=your_phone_id
GEMINI_API_KEY=your_gemini_key
WEBHOOK_VERIFY_TOKEN=your_verify_token
📁 Project Structure
plain
TruckHisaab/
├── backend/
│   ├── src/
│   │   ├── index.js                 # Entry point
│   │   ├── controllers/
│   │   │   └── webhookController.js # Main bot logic
│   │   ├── services/
│   │   │   ├── aiService.js         # Gemini AI integration
│   │   │   └── messageService.js    # Meta WhatsApp API
│   │   ├── models/
│   │   │   ├── User.js
│   │   │   ├── Trip.js
│   │   │   └── Expense.js
│   │   └── utils/
│   │       └── helpers.js
│   ├── package.json
│   └── .env.example
├── mobile/                          # React Native (Phase 2)
├── docs/                            # Documentation
├── assets/                          # Logo, images
└── README.md
🤝 Contributing
Abhi solo founder hoon, but jaldi team banegi! Agar aap bhi Indian logistics tech mein interest rakhte hain:
Fork karo
Branch banao (git checkout -b feature/xyz)
Commit karo (git commit -m 'Add xyz')
Push karo (git push origin feature/xyz)
Pull Request khol do
Areas where help chahiye:
🎨 UI/UX Design - Mobile app screens
🤖 AI/NLP - Hindi/Hinglish intent detection
📱 React Native - Mobile app development
📊 Data Analytics - Trucking insights dashboard
🎥 Content - YouTube Shorts, dhaba marketing
📞 Contact & Support
Table
Channel	Link/Number
WhatsApp Bot	+91 88796 88678
Support Hours:
Bot: 24/7 (automated)
Human Support: 9 AM - 9 PM IST (WhatsApp)
🙏 Acknowledgements
Meta - WhatsApp Cloud API
Google - Gemini AI for Hindi/Hinglish
Render - Free hosting
MongoDB - Free database tier
Indian Truckers - Jo daily road pe struggle karte hain, unke liye ye product
<p align="center">
  <b>Truck ka hisaab, ab aasaan</b> 🚛💨
</p>
<p align="center">
  Made with love in India for 2.5 Crore Truckers
</p>

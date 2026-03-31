import React, { useEffect, useState } from 'react';
import { motion, useScroll, useTransform } from 'framer-motion';
import { Link } from 'react-router-dom';
import { ArrowRight, Sparkles, MapPin, Users, ShieldCheck, Bot } from 'lucide-react';

const LandingScreen = () => {
  const { scrollYProgress } = useScroll();
  const opacity = useTransform(scrollYProgress, [0, 0.5], [1, 0]);

  const AnimatedGlobe = () => (
    <div className="relative w-full max-w-[400px] aspect-square mx-auto">
      {/* Glow */}
      <div className="absolute inset-0 bg-primary-500/10 blur-[100px] rounded-full mix-blend-multiply" />
      
      {/* Spinning rings */}
      <div className="absolute inset-4 rounded-full border-2 border-primary-500/10 border-dashed spin-slow" />
      <div className="absolute inset-8 rounded-full border border-purple-500/20 spin-reverse-slow" />
      <div className="absolute inset-12 rounded-full border-t-2 border-l-2 border-primary-500/30 spin-slow" style={{ animationDuration: '8s' }} />
      
      {/* Center glowing element */}
      <div className="absolute inset-0 m-auto w-32 h-32 bg-primary-100 blur-[40px] rounded-full animate-pulse" />
      <div className="absolute inset-0 m-auto w-16 h-16 bg-white rounded-full border border-slate-200 flex items-center justify-center shadow-2xl">
        <Sparkles className="text-primary-600 w-8 h-8" />
      </div>

      {/* Floating cards */}
      <motion.div 
        animate={{ y: [0, -20, 0] }}
        transition={{ repeat: Infinity, duration: 4, ease: "easeInOut" }}
        className="absolute top-10 -left-10 bg-white border border-slate-200 p-4 rounded-2xl flex items-center gap-3 shadow-xl z-20"
      >
        <div className="bg-primary-50 p-2 rounded-xl border border-primary-100"><MapPin size={24} className="text-primary-600" /></div>
        <div>
          <p className="text-sm font-bold text-black">New York, NY</p>
          <p className="text-xs text-slate-600 font-bold">95% Match</p>
        </div>
      </motion.div>

      <motion.div 
        animate={{ y: [0, 20, 0] }}
        transition={{ repeat: Infinity, duration: 5, ease: "easeInOut", delay: 1 }}
        className="absolute bottom-10 -right-10 bg-white border border-slate-200 p-4 rounded-2xl flex items-center gap-3 shadow-xl z-20"
      >
        <div className="bg-purple-50 p-2 rounded-xl border border-purple-100"><Users size={24} className="text-purple-600" /></div>
        <div>
          <p className="text-sm font-bold text-black">Sarah & Mike</p>
          <p className="text-xs text-slate-600 font-bold">AI Verified</p>
        </div>
      </motion.div>
    </div>
  );

  return (
    <div className="min-h-screen bg-white overflow-hidden selection:bg-primary-500/20">
      {/* Navigation Bar */}
      <nav className="fixed top-0 w-full bg-white/80 backdrop-blur-lg z-50 border-b border-slate-100">
        <div className="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-10 h-10 bg-primary-600 rounded-xl flex items-center justify-center shadow-lg">
              <span className="text-white font-bold text-xl">R</span>
            </div>
            <span className="text-2xl font-heading font-bold tracking-tight text-black">Roomshare AI <span className="text-primary-600">Rental App</span></span>
          </div>
          <div className="flex items-center gap-6">
            <Link to="/login" className="text-slate-600 hover:text-black font-bold transition-colors">Log In</Link>
            <Link to="/signup" className="bg-black text-white px-6 py-2.5 rounded-full font-bold hover:bg-slate-800 transition-all shadow-lg hover:shadow-black/20">
              Get Started
            </Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="relative pt-32 pb-20 md:pt-48 md:pb-32 px-6 overflow-hidden">
        {/* Abstract Background Gradients */}
        <div className="absolute top-0 -left-[20%] w-[50%] h-[500px] bg-primary-600/20 blur-[120px] rounded-full pointer-events-none" />
        <div className="absolute top-[20%] -right-[20%] w-[50%] h-[500px] bg-purple-600/20 blur-[120px] rounded-full pointer-events-none" />

        <div className="max-w-7xl mx-auto grid md:grid-cols-2 gap-12 items-center relative z-10">
          <motion.div 
            initial={{ opacity: 0, x: -50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.8, ease: "easeOut" }}
            className="flex flex-col gap-6"
          >
            <div className="inline-flex items-center gap-2 bg-primary-50 border border-primary-200 text-primary-700 px-4 py-2 rounded-full w-max text-sm font-bold">
              <Sparkles size={16} />
              <span>AI-Powered Matchmaking 2.0</span>
            </div>
            
            <h1 className="text-5xl md:text-7xl font-bold font-heading leading-tight text-black">
              Find Your Perfect <br />
              <span className="text-primary-600">Living Space</span>
            </h1>
            
            <p className="text-lg md:text-xl text-slate-600 block max-w-lg font-medium leading-relaxed">
              Experience the future of renting. Our AI analyzes your lifestyle, preferences, and compatibility to find the perfect room and roommates.
            </p>
            
            <div className="flex flex-col sm:flex-row gap-4 mt-4">
              <Link to="/signup" className="flex items-center justify-center gap-2 bg-primary-600 hover:bg-primary-700 text-white px-8 py-4 rounded-full font-bold text-lg transition-all shadow-xl shadow-primary-200 hover:scale-[1.02]">
                Find a Room <ArrowRight size={20} />
              </Link>
              <Link to="/list-room" className="flex items-center justify-center gap-2 bg-white hover:bg-slate-50 text-black px-8 py-4 rounded-full font-bold text-lg transition-all border border-slate-200 shadow-md">
                List a Room
              </Link>
            </div>

            <div className="flex items-center gap-6 mt-8">
              <div className="flex -space-x-4">
                {[1, 2, 3, 4].map((i) => (
                  <div key={i} className="w-12 h-12 rounded-full border-2 border-white bg-slate-100" />
                ))}
              </div>
              <div className="text-sm">
                <p className="text-black font-bold">10,000+</p>
                <p className="text-slate-600 font-bold">Happy Roommates</p>
              </div>
            </div>
          </motion.div>

          <motion.div 
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 1, ease: "easeOut", delay: 0.2 }}
            className="relative"
          >
            <AnimatedGlobe />
          </motion.div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-24 px-6 relative z-10 bg-slate-50 border-t border-slate-100">
        <div className="max-w-7xl mx-auto">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-5xl font-bold font-heading mb-4 text-black">Why Choose <span className="text-primary-600">Roomshare AI</span>?</h2>
            <p className="text-slate-600 font-bold">We take the guesswork out of finding a place to live.</p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            {[
              {
                icon: Bot,
                title: "Smart Compatibility",
                desc: "Our AI matches you with roommates based on lifestyle habits, sleep schedules, and cleaning preferences."
              },
              {
                icon: ShieldCheck,
                title: "Verified Identity",
                desc: "Every user undergoes background and identity verification ensuring a safe environment for everyone."
              },
              {
                icon: MapPin,
                title: "Budget & Location",
                desc: "Real-time market insights help you find the best rooms available within your exact budget."
              }
            ].map((feature, i) => (
              <motion.div 
                key={i}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true, margin: "-100px" }}
                transition={{ duration: 0.6, delay: i * 0.2 }}
                className="bg-white border border-slate-200 p-8 rounded-3xl hover:border-primary-500/50 hover:shadow-2xl transition-all group"
              >
                <div className="w-14 h-14 bg-primary-50 border border-primary-100 rounded-2xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform">
                  <feature.icon size={28} className="text-primary-600" />
                </div>
                <h3 className="text-xl font-bold mb-3 text-black">{feature.title}</h3>
                <p className="text-slate-600 leading-relaxed font-medium">{feature.desc}</p>
              </motion.div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
};

export default LandingScreen;

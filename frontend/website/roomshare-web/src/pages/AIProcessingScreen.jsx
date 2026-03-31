import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, useAnimation } from 'framer-motion';
import { Sparkles, Bot, Cpu, Database, Search, CheckCircle2 } from 'lucide-react';

const AIProcessingScreen = () => {
  const [progress, setProgress] = useState(0);
  const [status, setStatus] = useState('Initializing AI Matchmaker...');
  const navigate = useNavigate();

  const statuses = [
    'Initializing AI Matchmaker...',
    'Analyzing your lifestyle preferences...',
    'Scanning 50,000+ local profiles...',
    'Calculating compatibility scores...',
    'Filtering for budget and location...',
    'Verifying identity & trust markers...',
    'Finalizing your perfect matches...'
  ];

  useEffect(() => {
    const duration = 4000; // 4 seconds
    const interval = 40;
    const increment = 100 / (duration / interval);

    const timer = setInterval(() => {
      setProgress(prev => {
        if (prev >= 100) {
          clearInterval(timer);
          return 100;
        }
        return prev + increment;
      });
    }, interval);

    const statusInterval = setInterval(() => {
      setStatus(prev => {
        const currentIndex = statuses.indexOf(prev);
        if (currentIndex < statuses.length - 1) {
          return statuses[currentIndex + 1];
        }
        return prev;
      });
    }, duration / statuses.length);

    setTimeout(() => {
      navigate('/ai-results');
    }, duration + 1000);

    return () => {
      clearInterval(timer);
      clearInterval(statusInterval);
    };
  }, [navigate]);

  return (
    <div className="min-h-screen bg-white flex flex-col items-center justify-center p-8 relative overflow-hidden">
      {/* Background Atmosphere */}
      <div className="absolute inset-0 z-0">
        <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-primary-500/10 blur-[120px] rounded-full animate-pulse" />
        <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-purple-500/10 blur-[120px] rounded-full animate-pulse" style={{ animationDelay: '1s' }} />
      </div>

      <div className="relative z-10 w-full max-w-lg text-center">
        {/* Animated AI Core */}
        <div className="relative w-48 h-48 mx-auto mb-12">
          <motion.div 
            animate={{ rotate: 360 }}
            transition={{ duration: 8, repeat: Infinity, ease: "linear" }}
            className="absolute inset-0 border-4 border-dashed border-primary-200 rounded-full"
          />
          <motion.div 
            animate={{ rotate: -360 }}
            transition={{ duration: 12, repeat: Infinity, ease: "linear" }}
            className="absolute inset-4 border-2 border-dashed border-purple-200 rounded-full"
          />
          
          <div className="absolute inset-8 bg-gradient-to-tr from-primary-600 to-purple-600 rounded-full flex items-center justify-center shadow-2xl shadow-primary-500/40 overflow-hidden">
            <motion.div
              animate={{ scale: [1, 1.1, 1] }}
              transition={{ duration: 2, repeat: Infinity, ease: "easeInOut" }}
            >
              <Bot size={64} className="text-white" />
            </motion.div>
            
            {/* Liquid Progress Fill */}
            <motion.div 
              className="absolute bottom-0 left-0 w-full bg-white/20"
              initial={{ height: '0%' }}
              animate={{ height: `${progress}%` }}
              transition={{ ease: "linear" }}
            />
          </div>
          
          {/* Pulsing Rings */}
          <motion.div 
            animate={{ scale: [1, 1.5, 1], opacity: [0.5, 0, 0.5] }}
            transition={{ duration: 2, repeat: Infinity }}
            className="absolute inset-0 border-2 border-primary-500 rounded-full"
          />
        </div>

        <h1 className="text-3xl font-bold font-heading mb-3 text-black">
          AI is <span className="text-primary-600">Thinking</span>
        </h1>
        
        <p className="text-slate-600 font-bold mb-12 h-6">{status}</p>

        {/* Progress Bar Container */}
        <div className="bg-slate-100 h-3 w-full rounded-full overflow-hidden mb-4 border border-slate-200 shadow-inner">
          <motion.div 
            className="bg-gradient-to-r from-primary-600 via-primary-500 to-purple-600 h-full rounded-full"
            initial={{ width: '0%' }}
            animate={{ width: `${progress}%` }}
            transition={{ ease: "linear" }}
          />
        </div>
        
        <div className="flex justify-between items-center text-xs font-black tracking-widest text-primary-600 uppercase">
          <span>{Math.round(progress)}% Complete</span>
          <span className="flex items-center gap-1">
            <Cpu size={12} className="animate-spin" /> Neural Link Active
          </span>
        </div>

        {/* Floating Icons for Atmosphere */}
        <div className="mt-16 grid grid-cols-4 gap-8 opacity-20">
          <Database size={24} className="mx-auto" />
          <Search size={24} className="mx-auto" />
          <Sparkles size={24} className="mx-auto" />
          <CheckCircle2 size={24} className="mx-auto" />
        </div>
      </div>
    </div>
  );
};

export default AIProcessingScreen;

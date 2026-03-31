import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ArrowLeft, Send, Sparkles, 
  Bot, User, MessageCircle, 
  ShieldCheck, Zap, Info 
} from 'lucide-react';

const AIAssistantScreen = () => {
  const navigate = useNavigate();
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState([
    { 
      id: 1, 
      text: "Hello! I'm your RoomShare AI Assistant. How can I help you today?", 
      isFromAI: true,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    },
    { 
      id: 2, 
      text: "I can help you find roommates, list your property, or answer questions about compatibility scores.", 
      isFromAI: true,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    }
  ]);
  const [isTyping, setIsTyping] = useState(false);
  const scrollRef = useRef(null);

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [messages, isTyping]);

  const handleSend = () => {
    if (!input.trim()) return;

    const userMsg = {
      id: Date.now(),
      text: input,
      isFromAI: false,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    };

    setMessages(prev => [...prev, userMsg]);
    setInput('');
    setIsTyping(true);

    // Simulate AI response
    setTimeout(() => {
      let aiResponse = "That's a great question! Based on your profile, I recommend looking at matches in Pacific Heights where compatibility is currently 85% higher.";
      
      if (input.toLowerCase().includes('roommate')) {
        aiResponse = "I've analyzed 240+ profiles in your area. You have 3 'Platinum' matches waiting in your Results screen!";
      } else if (input.toLowerCase().includes('price') || input.toLowerCase().includes('budget')) {
        aiResponse = "Current San Francisco averages for shared lofts are around $1,600. Your $1,800 budget puts you in the top 10% of premium listings.";
      }

      setMessages(prev => [...prev, {
        id: Date.now() + 1,
        text: aiResponse,
        isFromAI: true,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      }]);
      setIsTyping(false);
    }, 1500);
  };

  return (
    <div className="bg-slate-50 h-screen flex flex-col">
      {/* Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200 flex items-center justify-between z-10">
        <div className="flex items-center gap-4">
          <button 
            onClick={() => navigate(-1)}
            className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
          >
            <ArrowLeft size={20} className="text-black" />
          </button>
          <div className="flex items-center gap-2">
             <div className="w-10 h-10 bg-primary-600 rounded-xl flex items-center justify-center text-white shadow-lg shadow-primary-500/20">
                <Bot size={24} />
             </div>
             <div>
                <h1 className="text-lg font-black text-black">Roomshare AI</h1>
                <div className="flex items-center gap-1.5">
                  <div className="w-1.5 h-1.5 bg-green-500 rounded-full animate-pulse" />
                  <span className="text-[10px] font-black text-slate-400 uppercase tracking-widest">Always Learning</span>
                </div>
             </div>
          </div>
        </div>
        <div className="flex gap-2">
           <div className="w-10 h-10 bg-slate-50 border border-slate-100 rounded-full flex items-center justify-center text-primary-500">
              <ShieldCheck size={20} />
           </div>
        </div>
      </div>

      {/* Chat Area */}
      <div 
        ref={scrollRef}
        className="flex-1 overflow-y-auto p-6 space-y-6 scroll-smooth"
      >
        <AnimatePresence>
          {messages.map((msg) => (
            <motion.div
              key={msg.id}
              initial={{ opacity: 0, y: 10, scale: 0.95 }}
              animate={{ opacity: 1, y: 0, scale: 1 }}
              className={`flex ${msg.isFromAI ? 'justify-start' : 'justify-end'} group`}
            >
              <div className={`flex gap-3 max-w-[85%] ${msg.isFromAI ? 'flex-row' : 'flex-row-reverse'}`}>
                {msg.isFromAI && (
                   <div className="w-8 h-8 bg-primary-50 rounded-lg flex items-center justify-center text-primary-600 shrink-0 mt-1 mt-auto">
                    <Sparkles size={16} />
                  </div>
                )}
                <div className="space-y-1">
                  <div className={`p-4 rounded-[24px] shadow-sm text-sm font-medium leading-relaxed ${
                    msg.isFromAI 
                      ? 'bg-white text-slate-700 border border-slate-100 rounded-bl-none' 
                      : 'bg-black text-white rounded-br-none shadow-xl shadow-black/10'
                  }`}>
                    {msg.text}
                  </div>
                  <p className={`text-[10px] font-black uppercase tracking-widest text-slate-300 ${msg.isFromAI ? 'text-left' : 'text-right'}`}>
                    {msg.timestamp}
                  </p>
                </div>
              </div>
            </motion.div>
          ))}
          {isTyping && (
            <motion.div
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              className="flex justify-start"
            >
              <div className="bg-white border border-slate-100 p-4 rounded-[24px] rounded-bl-none shadow-sm flex gap-1">
                <motion.div animate={{ opacity: [0.4, 1, 0.4] }} transition={{ repeat: Infinity, duration: 1 }} className="w-1.5 h-1.5 bg-primary-400 rounded-full" />
                <motion.div animate={{ opacity: [0.4, 1, 0.4] }} transition={{ repeat: Infinity, duration: 1, delay: 0.2 }} className="w-1.5 h-1.5 bg-primary-400 rounded-full" />
                <motion.div animate={{ opacity: [0.4, 1, 0.4] }} transition={{ repeat: Infinity, duration: 1, delay: 0.4 }} className="w-1.5 h-1.5 bg-primary-400 rounded-full" />
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </div>

      {/* Input Area */}
      <div className="p-6 bg-white border-t border-slate-100">
        <div className="max-w-4xl mx-auto flex gap-3 relative">
          <input 
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSend()}
            placeholder="Search, match, or ask anything..."
            className="flex-1 bg-slate-50 border-2 border-slate-100 rounded-[32px] px-8 py-5 font-bold text-black focus:border-primary-500 focus:outline-none focus:bg-white transition-all shadow-inner placeholder:text-slate-300"
          />
          <button 
            onClick={handleSend}
            disabled={!input.trim()}
            className="w-14 h-14 bg-black text-white rounded-full flex items-center justify-center transition-all hover:scale-110 active:scale-95 disabled:opacity-20 disabled:scale-100 shadow-xl"
          >
            <Send size={24} className="-rotate-12" />
          </button>
        </div>
        
        {/* Suggestion Chips */}
        <div className="max-w-4xl mx-auto flex gap-2 mt-4 overflow-x-auto pb-2 scrollbar-none">
          {["Find Roommates", "Price Trends", "Safety Tips", "Lease Help"].map(chip => (
            <button 
              key={chip}
              onClick={() => setInput(chip)}
              className="px-4 py-2 bg-slate-100 rounded-full text-[10px] font-black text-slate-500 uppercase tracking-widest hover:bg-primary-50 hover:text-primary-600 transition-colors shrink-0 whitespace-nowrap"
            >
              {chip}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AIAssistantScreen;

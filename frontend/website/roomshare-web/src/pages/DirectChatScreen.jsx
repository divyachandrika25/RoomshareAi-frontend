import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ArrowLeft, Search, Phone, 
  Video, MoreVertical, Send, 
  Sparkles, ShieldCheck, Image,
  Paperclip, Mic, CheckCircle2 
} from 'lucide-react';

const DirectChatScreen = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [input, setInput] = useState('');
  const scrollRef = useRef(null);

  const [messages, setMessages] = useState([
    { id: 1, text: "Hey! I'm interested in the room at Pacific Heights. Is it still available?", sender: "me", time: "10:30 AM", status: "read" },
    { id: 2, text: "Yes it is! I just finished the AI compatibility review for our group. You scored 98% which is amazing.", sender: "other", time: "10:32 AM" },
    { id: 3, text: "That's great to hear! Would you be open to a tour this weekend?", sender: "me", time: "10:35 AM", status: "sent" }
  ]);

  const user = {
    name: "Mike Chen",
    status: "Online",
    photo: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=200"
  };

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSend = () => {
    if (!input.trim()) return;
    const newMsg = {
      id: Date.now(),
      text: input,
      sender: "me",
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      status: "sent"
    };
    setMessages([...messages, newMsg]);
    setInput('');
  };

  return (
    <div className="bg-slate-50 h-screen flex flex-col">
      {/* Header */}
      <div className="bg-white px-4 py-4 border-b border-slate-200 flex items-center justify-between z-10 shadow-sm">
        <div className="flex items-center gap-3">
          <button 
            onClick={() => navigate(-1)}
            className="w-10 h-10 bg-slate-50 rounded-full flex items-center justify-center hover:bg-slate-100 transition-colors"
          >
            <ArrowLeft size={20} className="text-black" />
          </button>
          
          <div className="flex items-center gap-3">
             <div className="relative">
                <img src={user.photo} alt={user.name} className="w-11 h-11 rounded-2xl object-cover shadow-md" />
                <div className="absolute -bottom-0.5 -right-0.5 w-3.5 h-3.5 bg-green-500 rounded-full border-2 border-white shadow-sm" />
             </div>
             <div>
                <h2 className="text-sm font-black text-black leading-none mb-1">{user.name}</h2>
                <span className="text-[10px] font-black text-green-500 uppercase tracking-widest">{user.status}</span>
             </div>
          </div>
        </div>

        <div className="flex items-center gap-1">
           <button className="w-10 h-10 bg-slate-50 rounded-full flex items-center justify-center text-slate-500 hover:text-primary-600 transition-colors">
              <Phone size={18} />
           </button>
           <button className="w-10 h-10 bg-slate-50 rounded-full flex items-center justify-center text-slate-500 hover:text-primary-600 transition-colors">
              <Video size={18} />
           </button>
           <button className="w-10 h-10 bg-slate-50 rounded-full flex items-center justify-center text-slate-500 hover:text-primary-600 transition-colors">
              <MoreVertical size={18} />
           </button>
        </div>
      </div>

      {/* AI Trust Banner */}
      <div className="bg-primary-600 p-3 flex justify-center items-center gap-2">
         <ShieldCheck size={14} className="text-primary-200" />
         <span className="text-[9px] font-black text-white uppercase tracking-widest">End-to-end Encrypted • 98% AI Trust Score</span>
      </div>

      {/* Messages Area */}
      <div 
        ref={scrollRef}
        className="flex-1 overflow-y-auto p-6 space-y-6 scroll-smooth"
      >
        <div className="flex flex-col items-center mb-8">
           <div className="px-4 py-1 bg-slate-200/50 rounded-full text-[9px] font-black text-slate-500 uppercase tracking-widest mb-4">Today</div>
           <div className="bg-amber-50 border border-amber-100 p-4 rounded-3xl max-w-xs text-center space-y-2 mb-4">
              <Sparkles size={20} className="text-amber-500 mx-auto" />
              <p className="text-[11px] font-bold text-amber-900 leading-relaxed">
                Roommate Tip: Mike prefers a quiet environment after 10 PM. Keeping it chill will boost your harmony score!
              </p>
           </div>
        </div>

        <AnimatePresence>
          {messages.map((msg) => (
            <motion.div
              key={msg.id}
              initial={{ opacity: 0, y: 10, scale: 0.95 }}
              animate={{ opacity: 1, y: 0, scale: 1 }}
              className={`flex ${msg.sender === 'me' ? 'justify-end' : 'justify-start'}`}
            >
              <div className={`max-w-[75%] space-y-1 ${msg.sender === 'me' ? 'items-end' : 'items-start'}`}>
                <div className={`p-4 rounded-[24px] shadow-sm text-sm font-medium leading-relaxed ${
                  msg.sender === 'me' 
                    ? 'bg-primary-600 text-white rounded-br-none shadow-xl shadow-primary-500/10' 
                    : 'bg-white text-slate-700 border border-slate-100 rounded-bl-none'
                }`}>
                  {msg.text}
                </div>
                <div className={`flex items-center gap-1 ${msg.sender === 'me' ? 'justify-end' : 'justify-start'}`}>
                   <span className="text-[9px] font-black text-slate-300 uppercase tracking-widest">{msg.time}</span>
                   {msg.sender === 'me' && (
                     <CheckCircle2 size={10} className={msg.status === 'read' ? 'text-primary-500' : 'text-slate-300'} />
                   )}
                </div>
              </div>
            </motion.div>
          ))}
        </AnimatePresence>
      </div>

      {/* Input Area */}
      <div className="p-6 bg-white border-t border-slate-100">
        <div className="max-w-4xl mx-auto flex items-center gap-4">
          <div className="flex gap-1">
             <button className="w-10 h-10 text-slate-300 hover:text-primary-500 transition-colors">
                <Plus size={24} />
             </button>
             <button className="w-10 h-10 text-slate-300 hover:text-primary-500 transition-colors">
                <Image size={24} />
             </button>
          </div>
          
          <div className="flex-1 relative">
            <input 
              type="text"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSend()}
              placeholder="Start typing..."
              className="w-full bg-slate-50 border-2 border-slate-100 rounded-[32px] px-8 py-4 font-bold text-black focus:border-primary-500 focus:outline-none focus:bg-white transition-all shadow-inner"
            />
            <button className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-300 hover:text-primary-500">
               <Mic size={20} />
            </button>
          </div>

          <button 
            onClick={handleSend}
            disabled={!input.trim()}
            className={`w-14 h-14 rounded-2xl flex items-center justify-center transition-all shadow-xl shadow-primary-500/20 active:scale-95 ${input.trim() ? 'bg-primary-600 text-white scale-100' : 'bg-slate-100 text-slate-300 scale-95 opacity-50'}`}
          >
            <Send size={24} className="-rotate-12 translate-x-0.5" />
          </button>
        </div>
      </div>
    </div>
  );
};

export default DirectChatScreen;

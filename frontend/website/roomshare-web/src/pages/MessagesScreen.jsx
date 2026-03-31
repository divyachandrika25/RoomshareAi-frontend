import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ArrowLeft, Search, MessageCircle, 
  Settings, Bot, Plus, X, 
  ChevronRight, Sparkles, Filter 
} from 'lucide-react';

const MessagesScreen = () => {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 900);
  }, []);

  const chats = [
    {
      id: "ai-assistant",
      name: "Roomshare AI",
      lastMessage: "Based on your bio, I've found 3 new matches!",
      time: "Just now",
      photo: null,
      isAI: true,
      unread: 1,
      isOnline: true
    },
    {
      id: 1,
      name: "Mike Chen",
      lastMessage: "Hey! The apartment tour was great. What did you think?",
      time: "2h ago",
      photo: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=200",
      isAI: false,
      unread: 2,
      isOnline: true
    },
    {
      id: 2,
      name: "SoMa Seekers Group",
      lastMessage: "Sarah: I love the kitchen in that studio!",
      time: "5h ago",
      photo: "https://images.unsplash.com/photo-1529156069898-49953e39b30c?auto=format&fit=crop&q=80&w=200",
      isAI: false,
      unread: 0,
       isGroup: true,
      isOnline: false
    },
    {
      id: 3,
      name: "Jessica White",
      lastMessage: "Would you be open to moving in by Nov 1st?",
      time: "Yesterday",
      photo: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80&w=200",
      isAI: false,
      unread: 0,
      isOnline: false
    }
  ];

  const filteredChats = chats.filter(c => 
    c.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
    c.lastMessage.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="bg-slate-50 min-h-screen pb-24">
      {/* Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200 sticky top-0 z-40">
        <div className="flex items-center justify-between mb-8">
          <div className="flex items-center gap-4">
             <div className="w-12 h-12 bg-primary-600 rounded-[20px] flex items-center justify-center text-white shadow-lg shadow-primary-500/20">
                <MessageCircle size={24} />
             </div>
             <div>
                <h1 className="text-2xl font-black text-black font-heading tracking-tight">Messages</h1>
                <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">3 Active conversations</p>
             </div>
          </div>
          <button 
            onClick={() => navigate('/account-settings')}
            className="w-10 h-10 bg-slate-50 border border-slate-100 rounded-full flex items-center justify-center text-slate-400 hover:text-primary-500 transition-colors"
          >
            <Settings size={20} />
          </button>
        </div>

        {/* Search Bar */}
        <div className="relative group max-w-2xl mx-auto">
          <div className="absolute left-6 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-primary-500 transition-colors">
            <Search size={20} />
          </div>
          <input 
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search conversations..."
            className="w-full bg-slate-50 border-2 border-slate-100 rounded-[32px] py-4 pl-16 pr-8 font-bold text-black focus:border-primary-500 focus:outline-none focus:bg-white transition-all shadow-inner"
          />
          {searchQuery && (
            <button 
              onClick={() => setSearchQuery('')}
              className="absolute right-6 top-1/2 -translate-y-1/2 text-slate-300 hover:text-slate-500"
            >
              <X size={18} />
            </button>
          )}
        </div>
      </div>

      <div className="max-w-2xl mx-auto p-4 space-y-2">
        {isLoading ? (
          <div className="flex flex-col items-center justify-center py-32 space-y-4">
             <div className="w-14 h-14 bg-white rounded-3xl border border-slate-100 flex items-center justify-center shadow-sm">
                <div className="w-6 h-6 border-3 border-primary-500 border-t-transparent rounded-full animate-spin" />
             </div>
             <p className="font-black text-xs text-slate-400 uppercase tracking-widest">Encryption shaking hands...</p>
          </div>
        ) : filteredChats.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-32 text-center space-y-6">
             <div className="w-20 h-20 bg-slate-100 rounded-[32px] flex items-center justify-center text-slate-200">
                <MessageCircle size={40} />
             </div>
             <p className="text-slate-500 font-bold">No messages found for "{searchQuery}"</p>
          </div>
        ) : (
          <AnimatePresence>
            {filteredChats.map((chat, idx) => (
              <motion.div
                key={chat.id}
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: idx * 0.05 }}
                onClick={() => navigate(chat.isAI ? '/ai-assistant' : `/chat/${chat.id}`)}
                className={`bg-white p-5 rounded-[40px] border border-slate-100 flex items-center gap-6 group hover:shadow-xl hover:border-primary-100 transition-all cursor-pointer relative ${chat.unread > 0 ? 'bg-primary-50/20' : ''}`}
              >
                {/* Avatar with Status */}
                <div className="relative shrink-0">
                  <div className={`w-16 h-16 rounded-[28px] overflow-hidden shadow-lg border-2 ${chat.isAI ? 'bg-primary-600 border-primary-400 flex items-center justify-center text-white' : 'border-white'}`}>
                    {chat.isAI ? <Bot size={28} /> : <img src={chat.photo} alt={chat.name} className="w-full h-full object-cover" />}
                  </div>
                  {chat.isOnline && (
                    <div className="absolute -bottom-1 -right-1 w-5 h-5 bg-green-500 rounded-full border-4 border-white shadow-sm" />
                  )}
                </div>

                <div className="flex-1 min-w-0">
                  <div className="flex justify-between items-start mb-1">
                    <div className="flex items-center gap-2">
                      <h3 className="font-black text-black truncate group-hover:text-primary-600 transition-colors">{chat.name}</h3>
                      {chat.isAI && <Sparkles size={14} className="text-amber-500" />}
                      {chat.isGroup && <span className="text-[8px] font-black bg-slate-100 text-slate-500 px-1.5 py-0.5 rounded uppercase tracking-widest">Group</span>}
                    </div>
                    <span className="text-[10px] font-black text-slate-300 uppercase shrink-0 mt-1">{chat.time}</span>
                  </div>
                  <p className={`text-sm truncate leading-snug ${chat.unread > 0 ? 'font-bold text-slate-900' : 'font-medium text-slate-400'}`}>
                    {chat.lastMessage}
                  </p>
                </div>

                {chat.unread > 0 && (
                  <div className="bg-primary-600 text-white w-7 h-7 rounded-2xl flex items-center justify-center text-[10px] font-black shadow-lg shadow-primary-500/30 shrink-0">
                    {chat.unread}
                  </div>
                )}
                
                <ChevronRight size={18} className="text-slate-100 group-hover:text-primary-500 transition-all opacity-0 group-hover:opacity-100 -translate-x-2 group-hover:translate-x-0" />
              </motion.div>
            ))}
          </AnimatePresence>
        )}
      </div>

      {/* Floating Action Button */}
      <div className="fixed bottom-28 right-8 z-40">
        <button className="w-16 h-16 bg-black text-white rounded-[24px] flex items-center justify-center shadow-2xl hover:scale-110 active:scale-95 transition-all group">
          <Plus size={32} className="group-hover:rotate-90 transition-transform duration-500" />
        </button>
      </div>
    </div>
  );
};

export default MessagesScreen;

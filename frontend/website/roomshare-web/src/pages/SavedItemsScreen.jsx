import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ArrowLeft, Heart, MapPin, 
  Users, Home, Search, 
  ChevronRight, Sparkles, Filter,
  X 
} from 'lucide-react';

const SavedItemsScreen = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('Roommates');
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 800);
  }, []);

  const tabs = ['Roommates', 'Rooms'];

  const savedRoommates = [
    {
      id: 1,
      name: "Mike Chen",
      age: 26,
      photo: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=200",
      status: "HAS ROOM",
      match: 98,
      location: "Pacific Heights, SF"
    },
    {
      id: 2,
      name: "Sarah Jenkins",
      age: 24,
      photo: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80&w=200",
      status: "SEEKING",
      match: 94,
      location: "SoMa, SF"
    }
  ];

  const savedRooms = [
    {
      id: 1,
      name: "Luxury Loft",
      price: "$1,850",
      photo: "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80&w=400",
      type: "PRIVATE ROOM",
      location: "Nob Hill, SF"
    }
  ];

  return (
    <div className="bg-slate-50 min-h-screen pb-24">
      {/* Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200 sticky top-0 z-30">
        <div className="flex items-center justify-between mb-6">
          <button 
            onClick={() => navigate(-1)}
            className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
          >
            <ArrowLeft size={20} className="text-black" />
          </button>
          <h1 className="text-lg font-bold text-black">Saved Items</h1>
          <button className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors">
            <Search size={20} className="text-slate-500" />
          </button>
        </div>

        {/* Custom Tab Switcher */}
        <div className="flex bg-slate-100 p-1.5 rounded-[24px] gap-1 max-w-sm mx-auto shadow-inner">
          {tabs.map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`flex-1 flex items-center justify-center gap-2 py-3.5 rounded-[20px] text-xs font-black uppercase tracking-widest transition-all ${
                activeTab === tab 
                  ? 'bg-white text-black shadow-lg shadow-black/5' 
                  : 'text-slate-400 hover:text-slate-600'
              }`}
            >
              {tab === 'Roommates' ? <Users size={14} /> : <Home size={14} />}
              {tab}
            </button>
          ))}
        </div>
      </div>

      <div className="max-w-2xl mx-auto p-6">
        {isLoading ? (
          <div className="flex flex-col items-center justify-center py-32 space-y-4">
             <div className="w-12 h-12 border-4 border-primary-500 border-t-transparent rounded-full animate-spin" />
             <p className="font-bold text-slate-400">Fetching your favorites...</p>
          </div>
        ) : (
          <div className="space-y-6">
            <AnimatePresence mode="wait">
              {activeTab === 'Roommates' ? (
                <motion.div 
                  key="roommates"
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  exit={{ opacity: 0, x: 20 }}
                  className="space-y-4"
                >
                  {savedRoommates.map((person) => (
                    <div 
                      key={person.id}
                      onClick={() => navigate(`/profile/${person.id}`)}
                      className="bg-white p-5 rounded-[32px] border border-slate-100 shadow-sm flex items-center gap-5 group hover:border-primary-500/50 transition-all cursor-pointer"
                    >
                      <div className="relative shrink-0">
                        <img src={person.photo} alt={person.name} className="w-20 h-20 rounded-[28px] object-cover shadow-lg group-hover:scale-105 transition-transform" />
                        <div className="absolute -top-1 -right-1 bg-red-500 text-white w-6 h-6 rounded-full flex items-center justify-center border-2 border-white shadow-md">
                          <Heart size={12} fill="currentColor" />
                        </div>
                      </div>
                      <div className="flex-1">
                        <div className="flex justify-between items-start mb-1">
                          <h3 className="font-bold text-lg text-black">{person.name}, {person.age}</h3>
                          <div className="bg-primary-50 text-primary-600 px-2 py-1 rounded-lg text-[9px] font-black uppercase tracking-widest border border-primary-100">
                            {person.match}% MATCH
                          </div>
                        </div>
                        <p className="text-slate-400 text-xs font-medium flex items-center gap-1 mb-3">
                          <MapPin size={12} /> {person.location}
                        </p>
                        <div className="flex items-center justify-between">
                           <span className={`text-[9px] font-black uppercase tracking-widest px-2 py-1 rounded-md ${person.status === 'HAS ROOM' ? 'bg-green-50 text-green-600 border border-green-100' : 'bg-primary-50 text-primary-600 border border-primary-100'}`}>
                            {person.status}
                          </span>
                          <span className="text-primary-500 text-[10px] font-black uppercase tracking-wider group-hover:translate-x-1 transition-transform flex items-center gap-1">
                            Profile <ChevronRight size={14} />
                          </span>
                        </div>
                      </div>
                    </div>
                  ))}
                </motion.div>
              ) : (
                <motion.div 
                  key="rooms"
                  initial={{ opacity: 0, x: 20 }}
                  animate={{ opacity: 1, x: 0 }}
                  exit={{ opacity: 0, x: -20 }}
                  className="space-y-6"
                >
                   {savedRooms.map((room) => (
                    <div 
                      key={room.id}
                      onClick={() => navigate(`/room/${room.id}`)}
                      className="bg-white rounded-[40px] border border-slate-100 overflow-hidden shadow-sm group cursor-pointer hover:shadow-xl transition-all"
                    >
                      <div className="h-48 relative">
                        <img src={room.photo} alt={room.name} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700" />
                        <div className="absolute top-4 right-4 bg-white/20 backdrop-blur-md p-3 rounded-full border border-white/20 text-white hover:bg-red-500 hover:border-red-500 transition-all">
                          <Heart size={20} fill="currentColor" />
                        </div>
                        <div className="absolute bottom-4 left-4">
                           <div className="bg-primary-600 text-white px-4 py-2 rounded-2xl font-black text-sm shadow-xl">
                            {room.price} <span className="text-[10px] opacity-70">/mo</span>
                           </div>
                        </div>
                      </div>
                      <div className="p-6">
                        <div className="flex justify-between items-center mb-2">
                           <h3 className="font-bold text-xl text-black">{room.name}</h3>
                           <span className="text-[10px] font-black text-slate-400 uppercase tracking-widest border border-slate-100 px-2 py-1 rounded-lg">{room.type}</span>
                        </div>
                        <p className="text-slate-500 text-sm font-medium flex items-center gap-1">
                          <MapPin size={14} className="text-primary-500" /> {room.location}
                        </p>
                      </div>
                    </div>
                   ))}
                </motion.div>
              )}
            </AnimatePresence>
          </div>
        )}

        {!isLoading && (savedRoommates.length === 0 && savedRooms.length === 0) && (
          <div className="flex flex-col items-center justify-center py-32 text-center space-y-4">
             <div className="w-20 h-20 bg-slate-100 rounded-[32px] flex items-center justify-center text-slate-300">
                <Heart size={40} />
             </div>
             <h3 className="text-xl font-bold text-black">Your collection is empty</h3>
             <p className="text-slate-500 max-w-xs mx-auto text-sm font-medium">Save roommate profiles or room listings to see them here later.</p>
             <button 
              onClick={() => navigate('/home')}
              className="bg-black text-white px-8 py-4 rounded-3xl font-bold shadow-xl flex items-center gap-2 hover:scale-110 active:scale-95 transition-all"
             >
              <Search size={18} />
              Start Exploring
             </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default SavedItemsScreen;

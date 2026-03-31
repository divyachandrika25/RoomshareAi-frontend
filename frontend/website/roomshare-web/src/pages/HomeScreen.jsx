import React from 'react';
import { motion } from 'framer-motion';
import { MapPin, Star, Heart, Share2, Sparkles, Filter, Bell } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import { useAppContext } from '../context/AppContext';

const MOCK_ROOMS = [
  { id: 1, title: 'Luxury Appt near Downtown', location: 'Manhattan, NY', price: '$1,200', match: 98, image: 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=500&q=80', features: ['Private Bath', 'Gym', 'In-unit Laundry'] },
  { id: 2, title: 'Sunny Room with Balcony', location: 'Brooklyn, NY', price: '$950', match: 92, image: 'https://images.unsplash.com/photo-1502672260266-1c1de2d93688?w=500&q=80', features: ['Shared Bath', 'Pet Friendly'] },
  { id: 3, title: 'Modern Studio space', location: 'Queens, NY', price: '$1,100', match: 88, image: 'https://images.unsplash.com/photo-1502672023488-70e25813eb80?w=500&q=80', features: ['Private Bath', 'Doorman'] },
];

const HomeScreen = () => {
  const { toggleSaveRoom, isRoomSaved } = useAppContext();
  const navigate = useNavigate();

  return (
    <div className="p-6 md:p-12 pb-24 md:pb-12 min-h-screen">
      
      {/* Header Section */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-10 gap-6">
        <div>
          <h1 className="text-3xl md:text-5xl font-bold font-heading text-black">Discover Rooms</h1>
          <p className="text-slate-600 mt-2 font-medium">Find your perfect match powered by Roomshare AI</p>
        </div>
        <div className="flex gap-3">
          <Link to="/notifications" className="glass-panel p-3 rounded-xl hover:bg-slate-100 transition-colors relative border border-slate-200">
            <Bell size={20} className="text-black" />
            <span className="absolute top-2 right-2 w-2 h-2 bg-red-600 rounded-full shadow-[0_0_8px_rgba(220,38,38,0.5)]"></span>
          </Link>
          <Link to="/search" className="glass-panel p-3 rounded-xl hover:bg-slate-100 transition-colors border border-slate-200">
            <Filter size={20} className="text-black" />
          </Link>
          <div className="bg-primary-50 px-4 py-2 rounded-xl flex items-center gap-2 text-primary-700 border border-primary-200 shadow-sm">
            <Sparkles size={18} />
            <span className="font-bold text-sm">AI Magic Sorting</span>
          </div>
        </div>
      </div>

      {/* Featured AI Match */}
      <motion.div 
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="glass-panel p-6 rounded-3xl mb-12 flex flex-col md:flex-row gap-6 items-center relative overflow-hidden group border border-slate-200 shadow-xl cursor-pointer"
        onClick={() => navigate(`/room/${MOCK_ROOMS[0].id}`)}
      >
        <div className="absolute top-0 right-0 w-64 h-64 bg-primary-500/5 blur-[80px] rounded-full group-hover:bg-primary-500/10 transition-colors" />
        
        <div className="relative w-full md:w-1/3 aspect-[4/3] rounded-2xl overflow-hidden shadow-2xl">
          <img src={MOCK_ROOMS[0].image} alt="Featured" className="object-cover w-full h-full group-hover:scale-110 transition-transform duration-700" />
          <div className="absolute top-3 left-3 bg-black/60 backdrop-blur px-3 py-1 rounded-lg border border-white/20 text-sm font-bold flex items-center gap-1 shadow-xl">
            <Sparkles size={14} className="text-primary-400" />
            <span className="text-primary-300">Top Match</span>
          </div>
        </div>
        
        <div className="flex-1 relative z-10 w-full">
          <div className="flex justify-between items-start mb-2">
            <div>
              <h2 className="text-2xl font-bold font-heading text-black">{MOCK_ROOMS[0].title}</h2>
              <div className="flex items-center gap-1 text-slate-600 mt-1 font-medium">
                <MapPin size={16} className="text-primary-600" /> <span className="text-sm">{MOCK_ROOMS[0].location}</span>
              </div>
            </div>
            <div className="text-right">
              <span className="text-2xl font-bold text-black">{MOCK_ROOMS[0].price}<span className="text-sm text-slate-500 font-normal">/mo</span></span>
            </div>
          </div>
          
          <div className="flex flex-wrap gap-2 my-4">
            {MOCK_ROOMS[0].features.map((f, i) => (
              <span key={i} className="bg-white/10 border border-white/10 px-3 py-1 text-xs rounded-full text-slate-200 font-semibold">{f}</span>
            ))}
          </div>

          <div className="w-full bg-slate-100 h-2 rounded-full overflow-hidden mb-2 border border-slate-200">
             <motion.div initial={{ width: 0 }} animate={{ width: `${MOCK_ROOMS[0].match}%` }} transition={{ duration: 1, delay: 0.5 }} className="h-full bg-primary-600" />
          </div>
          <p className="text-xs text-primary-700 font-bold mb-6">{MOCK_ROOMS[0].match}% AI Compatibility Score</p>
          
          <div className="flex gap-3">
            <button 
              onClick={(e) => { e.stopPropagation(); navigate(`/room/${MOCK_ROOMS[0].id}/book`); }}
              className="flex-1 bg-black text-white font-bold py-3 rounded-xl hover:bg-slate-800 transition-all hover:scale-[1.02] shadow-xl text-sm"
            >
              Book a Tour
            </button>
            <button 
              onClick={(e) => { e.stopPropagation(); toggleSaveRoom(MOCK_ROOMS[0]); }}
              className={`p-3 rounded-xl transition-all border flex items-center justify-center ${
                isRoomSaved(MOCK_ROOMS[0].id) 
                  ? 'bg-red-50 border-red-200 text-red-600' 
                  : 'bg-slate-100 border-slate-200 text-slate-600 hover:bg-slate-200'
              }`}
            >
              <Heart size={20} className={isRoomSaved(MOCK_ROOMS[0].id) ? 'fill-current' : ''} />
            </button>
          </div>
        </div>
      </motion.div>

      <h3 className="text-2xl font-bold mb-8 text-black">More Rooms for You</h3>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {MOCK_ROOMS.slice(1).map((room, i) => (
          <motion.div 
            key={room.id}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 + (i * 0.1) }}
            whileHover={{ y: -8 }}
            className="glass-panel rounded-3xl overflow-hidden group cursor-pointer border border-slate-200 hover:border-primary-500/50 transition-all shadow-xl hover:shadow-2xl"
            onClick={() => navigate(`/room/${room.id}`)}
          >
            <div className="relative h-56 overflow-hidden">
              <img src={room.image} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" alt={room.title} />
              <div className="absolute top-4 right-4 bg-black/60 backdrop-blur px-3 py-1 rounded-lg text-xs font-bold border border-white/20 text-white shadow-xl">
                {room.match}% Match
              </div>
              <button 
                onClick={(e) => { e.stopPropagation(); toggleSaveRoom(room); }}
                className={`absolute top-4 left-4 p-2 rounded-xl backdrop-blur transition-all ${
                  isRoomSaved(room.id)
                    ? 'bg-red-500 text-white'
                    : 'bg-black/40 text-white hover:bg-black/60'
                }`}
              >
                <Heart size={18} className={isRoomSaved(room.id) ? 'fill-current' : ''} />
              </button>
            </div>
            <div className="p-6">
              <h4 className="font-bold text-xl mb-1 text-black">{room.title}</h4>
              <p className="text-sm text-slate-600 font-medium flex items-center gap-1 mb-4"><MapPin size={14} className="text-primary-600" />{room.location}</p>
              
              <div className="flex justify-between items-center mb-6">
                <p className="font-bold text-xl text-primary-700">{room.price}<span className="text-sm text-slate-500 font-normal">/mo</span></p>
                <div className="flex -space-x-3">
                  <div className="w-10 h-10 rounded-full bg-slate-100 border-2 border-white shadow-lg overflow-hidden">
                    <img src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&q=80" alt="avatar" />
                  </div>
                  <div className="w-10 h-10 rounded-full bg-slate-200 border-2 border-white shadow-lg overflow-hidden">
                    <img src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&q=80" alt="avatar" />
                  </div>
                </div>
              </div>

              <button 
                onClick={(e) => { e.stopPropagation(); navigate(`/room/${room.id}`); }}
                className="w-full bg-slate-100 hover:bg-black hover:text-white text-black font-bold py-3 rounded-xl flex items-center justify-center transition-all border border-slate-200"
              >
                View Details
              </button>
            </div>
          </motion.div>
        ))}
      </div>
    </div>
  );
};

export default HomeScreen;

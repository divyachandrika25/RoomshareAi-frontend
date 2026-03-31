import React from 'react';
import { motion } from 'framer-motion';
import { MapPin, Heart, ArrowLeft, Inbox } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import { useAppContext } from '../context/AppContext';

const SavedScreen = () => {
  const { savedRooms, toggleSaveRoom } = useAppContext();
  const navigate = useNavigate();

  return (
    <div className="p-6 md:p-12 pb-24 md:pb-12 min-h-screen">
      <div className="max-w-6xl mx-auto">
        <div className="flex items-center gap-4 mb-10">
          <button onClick={() => navigate(-1)} className="glass-panel p-3 rounded-xl hover:bg-slate-100 transition-colors border border-slate-200">
            <ArrowLeft size={20} className="text-black" />
          </button>
          <h1 className="text-3xl md:text-5xl font-bold font-heading text-black">Saved Rooms</h1>
        </div>

        {savedRooms.length === 0 ? (
          <motion.div 
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="flex flex-col items-center justify-center p-20 glass-panel rounded-3xl border border-slate-200 text-center shadow-xl"
          >
            <div className="p-6 bg-slate-50 rounded-full mb-6 border border-slate-200 shadow-inner">
              <Heart size={48} className="text-slate-400" />
            </div>
            <h2 className="text-2xl font-bold text-black mb-2">No saved rooms yet</h2>
            <p className="text-slate-600 mb-8 max-w-sm font-bold">Heart your favorite listings on the home page to see them here.</p>
            <Link to="/home" className="bg-black hover:bg-slate-800 text-white font-bold py-3 px-8 rounded-xl transition-all shadow-lg">
              Browse Rooms
            </Link>
          </motion.div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {savedRooms.map((room, i) => (
              <motion.div 
                key={room.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.1 * i }}
                className="glass-panel rounded-3xl overflow-hidden group border border-slate-200 hover:border-primary-500/50 transition-all shadow-xl hover:shadow-2xl"
              >
                <div className="relative h-52 overflow-hidden">
                  <img src={room.image} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" alt={room.title} />
                  <button 
                    onClick={() => toggleSaveRoom(room)}
                    className="absolute top-4 right-4 bg-red-500 text-white p-2.5 rounded-xl shadow-xl hover:scale-110 transition-all"
                  >
                    <Heart size={20} className="fill-current" />
                  </button>
                </div>
                <div className="p-6">
                  <h4 className="font-bold text-xl mb-1 text-black">{room.title}</h4>
                  <p className="text-sm text-slate-600 font-bold flex items-center gap-1 mb-6"><MapPin size={14} className="text-primary-600" />{room.location}</p>
                  <div className="flex justify-between items-center">
                    <p className="font-bold text-2xl text-primary-700">{room.price}<span className="text-sm text-slate-600 font-normal">/mo</span></p>
                    <Link to={`/room/${room.id}`} className="bg-primary-600 hover:bg-primary-700 text-white font-bold py-2.5 px-6 rounded-xl transition-all shadow-md text-sm">
                      View Room
                    </Link>
                  </div>
                </div>
              </motion.div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default SavedScreen;

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ArrowLeft, Search, Calendar, MapPin, 
  ChevronRight, Filter, Clock, CheckCircle2, 
  XCircle, Loader2, Home 
} from 'lucide-react';

const BookingHistoryScreen = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('All');
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 800);
  }, []);

  const tabs = ['All', 'Upcoming', 'Completed', 'Cancelled'];

  const bookings = [
    {
      id: 1,
      propertyName: "Pacific Heights Loft",
      address: "2450 Washington St, San Francisco",
      date: "Oct 25, 2024",
      time: "10:00 AM",
      status: "Upcoming",
      price: 1200,
      image: "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80&w=400"
    },
    {
      id: 2,
      propertyName: "Mission District Apartment",
      address: "123 Valencia St, San Francisco",
      date: "Sep 12, 2024",
      time: "02:30 PM",
      status: "Completed",
      price: 950,
      image: "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&q=80&w=400"
    },
    {
      id: 3,
      propertyName: "SoMa Creative Studio",
      address: "888 Howard St, San Francisco",
      date: "Aug 05, 2024",
      time: "11:00 AM",
      status: "Cancelled",
      price: 1100,
      image: "https://images.unsplash.com/photo-1493809842364-78817add7ffb?auto=format&fit=crop&q=80&w=400"
    }
  ];

  const filteredBookings = activeTab === 'All' 
    ? bookings 
    : bookings.filter(b => b.status === activeTab);

  const getStatusStyles = (status) => {
    switch (status) {
      case 'Upcoming': return 'bg-primary-50 text-primary-600 border-primary-100';
      case 'Completed': return 'bg-green-50 text-green-600 border-green-100';
      case 'Cancelled': return 'bg-red-50 text-red-600 border-red-100';
      default: return 'bg-slate-50 text-slate-600 border-slate-100';
    }
  };

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
          <h1 className="text-lg font-bold text-black">Booking History</h1>
          <button className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors">
            <Search size={20} className="text-slate-500" />
          </button>
        </div>

        {/* Tab Switcher */}
        <div className="flex bg-slate-100 p-1 rounded-2xl gap-1">
          {tabs.map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`flex-1 py-2.5 rounded-xl text-xs font-black uppercase tracking-widest transition-all ${
                activeTab === tab 
                  ? 'bg-white text-black shadow-sm' 
                  : 'text-slate-400 hover:text-slate-600'
              }`}
            >
              {tab}
            </button>
          ))}
        </div>
      </div>

      <div className="max-w-3xl mx-auto p-6">
        {isLoading ? (
          <div className="flex flex-col items-center justify-center py-20 text-slate-400 gap-4">
            <Loader2 size={32} className="animate-spin text-primary-500" />
            <p className="font-bold">Syncing your records...</p>
          </div>
        ) : filteredBookings.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 text-center space-y-4">
             <div className="w-20 h-20 bg-slate-100 rounded-3xl flex items-center justify-center text-slate-300">
              <Calendar size={40} />
            </div>
            <h3 className="text-xl font-bold text-black">No bookings found</h3>
            <p className="text-slate-500 max-w-xs mx-auto">You haven't made any bookings in this category yet.</p>
          </div>
        ) : (
          <div className="space-y-6">
            <AnimatePresence mode="popLayout">
              {filteredBookings.map((booking, i) => (
                <motion.div
                  key={booking.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, scale: 0.95 }}
                  transition={{ delay: i * 0.05 }}
                  className="bg-white rounded-[32px] border border-slate-200 shadow-sm overflow-hidden group hover:border-primary-500/50 transition-all cursor-pointer"
                  onClick={() => navigate(`/tour-details/${booking.id}`)}
                >
                  <div className="flex flex-col sm:flex-row">
                    <div className="w-full sm:w-40 h-40 shrink-0 relative">
                      <img 
                        src={booking.image} 
                        alt={booking.propertyName} 
                        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                      />
                      <div className="absolute top-3 left-3">
                         <div className={`px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-widest border backdrop-blur-md ${getStatusStyles(booking.status)}`}>
                          {booking.status}
                        </div>
                      </div>
                    </div>

                    <div className="p-6 flex-1 flex flex-col justify-between">
                      <div>
                        <div className="flex justify-between items-start mb-1">
                          <h3 className="font-bold text-lg text-black group-hover:text-primary-600 transition-colors">
                            {booking.propertyName}
                          </h3>
                          <span className="text-primary-600 font-black text-lg">${booking.price}</span>
                        </div>
                        <p className="text-xs text-slate-500 font-medium flex items-center gap-1 mb-4">
                          <MapPin size={12} /> {booking.address}
                        </p>
                      </div>

                      <div className="flex items-center justify-between pt-4 border-t border-slate-50">
                        <div className="flex items-center gap-4">
                          <div className="flex items-center gap-1.5 text-xs font-bold text-slate-600">
                            <Calendar size={14} className="text-primary-500" /> {booking.date}
                          </div>
                          <div className="flex items-center gap-1.5 text-xs font-bold text-slate-600">
                            <Clock size={14} className="text-primary-500" /> {booking.time}
                          </div>
                        </div>
                        <ChevronRight size={18} className="text-slate-300 group-hover:text-primary-500 transition-colors" />
                      </div>
                    </div>
                  </div>
                </motion.div>
              ))}
            </AnimatePresence>
          </div>
        )}
      </div>

      {/* Floating Action */}
      <div className="fixed bottom-8 right-8 z-40">
        <button 
          onClick={() => navigate('/home')}
          className="bg-black text-white px-6 py-4 rounded-full font-bold shadow-2xl flex items-center gap-2 hover:scale-110 active:scale-95 transition-all"
        >
          <Home size={20} />
          Go Home
        </button>
      </div>
    </div>
  );
};

export default BookingHistoryScreen;

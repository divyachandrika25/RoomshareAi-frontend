import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  User, Settings, Heart, History, 
  ShieldCheck, HelpCircle, LogOut, 
  ChevronRight, Camera, MapPin, 
  PlusCircle, Edit3, Star, Award
} from 'lucide-react';

const ProfileScreen = () => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);

  const user = {
    name: "John Doe",
    email: "john.doe@example.com",
    photo: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&q=80&w=200",
    isVerified: true,
    trustScore: 98,
    savedCount: 12,
    bookingCount: 4
  };

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 800);
  }, []);

  const menuItems = [
    { icon: PlusCircle, label: "List Your Room", path: "/list-room", color: "text-primary-600", bg: "bg-primary-50" },
    { icon: History, label: "Booking History", path: "/booking-history", color: "text-amber-600", bg: "bg-amber-50" },
    { icon: Heart, label: "Saved Items", path: "/saved-items", color: "text-red-600", bg: "bg-red-50" },
    { icon: ShieldCheck, label: "Privacy & Security", path: "/privacy-security", color: "text-green-600", bg: "bg-green-50" },
    { icon: HelpCircle, label: "Help Center", path: "#", color: "text-indigo-600", bg: "bg-indigo-50" },
    { icon: Settings, label: "Account Settings", path: "/account-settings", color: "text-slate-600", bg: "bg-slate-50" },
  ];

  return (
    <div className="bg-slate-50 min-h-screen pb-32">
      {/* Premium Header */}
      <div className="relative h-72 bg-black overflow-hidden sticky top-0 z-10">
        <div className="absolute inset-0 bg-gradient-to-br from-primary-900 via-primary-600 to-black opacity-60" />
        <div className="absolute inset-0 bg-[url('https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&q=80&w=1000')] bg-cover bg-center mix-blend-overlay" />
        
        <div className="absolute inset-x-0 bottom-0 p-8 flex items-end justify-between translate-y-4">
           <div className="flex items-center gap-6">
              <div className="relative group">
                 <div className="w-24 h-24 rounded-[32px] border-4 border-white shadow-2xl overflow-hidden">
                    <img src={user.photo} alt={user.name} className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500" />
                 </div>
                 <button className="absolute -bottom-1 -right-1 w-8 h-8 bg-white rounded-full flex items-center justify-center text-primary-600 shadow-lg border-2 border-primary-50">
                    <Camera size={16} />
                 </button>
              </div>
              <div className="mb-2">
                 <div className="flex items-center gap-2 mb-1">
                    <h1 className="text-3xl font-black text-white font-heading">{user.name}</h1>
                    {user.isVerified && <Award size={20} className="text-primary-400" />}
                 </div>
                 <p className="text-white/70 text-sm font-medium">{user.email}</p>
                 <div className="flex items-center gap-2 mt-2">
                    <span className="px-2 py-0.5 bg-white/20 backdrop-blur-md rounded-lg text-[9px] font-black text-white uppercase tracking-widest border border-white/20">Verified Identity</span>
                 </div>
              </div>
           </div>
           <button 
            onClick={() => navigate('/edit-profile')}
            className="w-12 h-12 bg-white/10 backdrop-blur-xl border border-white/20 rounded-2xl flex items-center justify-center text-white hover:bg-white hover:text-black transition-all mb-4"
           >
              <Edit3 size={20} />
           </button>
        </div>
      </div>

      <div className="max-w-4xl mx-auto px-6 -mt-4 relative z-20">
        {/* Quick Stats */}
        <div className="grid grid-cols-3 gap-4 mb-10">
           {[
             { val: user.savedCount, lab: "Saved Items", icon: Heart },
             { val: `${user.trustScore}%`, lab: "Trust Score", icon: ShieldCheck },
             { val: user.bookingCount, lab: "Bookings", icon: History }
           ].map((stat, i) => (
             <motion.div 
               key={i}
               initial={{ opacity: 0, y: 20 }}
               animate={{ opacity: 1, y: 0 }}
               transition={{ delay: i * 0.1 }}
               className="bg-white p-6 rounded-[32px] border border-slate-100 shadow-sm text-center group hover:shadow-xl transition-all"
             >
                <div className="w-10 h-10 bg-slate-50 rounded-2xl flex items-center justify-center mx-auto mb-3 text-slate-400 group-hover:text-primary-500 group-hover:bg-primary-50 transition-colors">
                   <stat.icon size={20} />
                </div>
                <h4 className="text-xl font-black text-black leading-none mb-1">{stat.val}</h4>
                <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">{stat.lab}</p>
             </motion.div>
           ))}
        </div>

        {/* Menu Sections */}
        <div className="space-y-8">
           <div>
              <h3 className="text-xs font-black text-slate-400 uppercase tracking-[0.2em] mb-6 px-2">Account Management</h3>
              <div className="bg-white rounded-[40px] border border-slate-100 overflow-hidden shadow-sm divide-y divide-slate-50">
                 {menuItems.map((item, i) => (
                   <button 
                    key={i}
                    onClick={() => item.path !== '#' && navigate(item.path)}
                    className="w-full flex items-center justify-between p-6 hover:bg-slate-50 transition-all group"
                   >
                      <div className="flex items-center gap-5">
                         <div className={`w-12 h-12 ${item.bg} ${item.color} rounded-2xl flex items-center justify-center group-hover:scale-110 transition-transform`}>
                            <item.icon size={22} />
                         </div>
                         <span className="text-sm font-black text-slate-700 uppercase tracking-wide">{item.label}</span>
                      </div>
                      <ChevronRight size={20} className="text-slate-100 group-hover:text-primary-500 group-hover:translate-x-1 transition-all" />
                   </button>
                 ))}
              </div>
           </div>

           {/* Danger Zone */}
           <div className="pt-4">
              <button 
                className="w-full bg-red-50 hover:bg-red-100 border border-red-100 p-6 rounded-[40px] flex items-center justify-center gap-3 text-red-600 transition-all group shadow-sm active:scale-95"
              >
                 <LogOut size={22} className="group-hover:-translate-x-1 transition-transform" />
                 <span className="text-sm font-black uppercase tracking-widest">Sign Out from Roomshare</span>
              </button>
           </div>
        </div>

        {/* App Info */}
        <div className="text-center py-12">
           <div className="flex items-center justify-center gap-2 mb-2">
              <span className="w-2 h-2 bg-primary-500 rounded-full" />
              <p className="text-[10px] font-black text-black uppercase tracking-widest">Roomshare AI Rental App</p>
           </div>
           <p className="text-[9px] font-black text-slate-300 uppercase tracking-widest">Version 2.4.0 • Build 8291</p>
        </div>
      </div>
    </div>
  );
};

export default ProfileScreen;

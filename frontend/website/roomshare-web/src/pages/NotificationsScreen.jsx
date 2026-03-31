import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ArrowLeft, Bell, Sparkles, 
  Settings, CheckCircle2, Info, 
  MapPin, UserPlus, Clock, 
  Trash2, X 
} from 'lucide-react';

const NotificationsScreen = () => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [notifications, setNotifications] = useState([
    {
      id: 1,
      type: 'match',
      title: "New 98% Compatibility Match!",
      message: "Mike Chen just created a profile that fits your 'Night Owl' lifestyle almost perfectly. Check it out!",
      time: "2 mins ago",
      isRead: false,
      icon: Sparkles
    },
    {
      id: 2,
      type: 'tour',
      title: "Tour Confirmed",
      message: "Your tour for 'Pacific Heights Loft' has been confirmed for tomorrow at 10:00 AM.",
      time: "1 hour ago",
      isRead: false,
      icon: CheckCircle2
    },
    {
      id: 3,
      type: 'group',
      title: "Group Invitation",
      message: "Sarah invited you to join the 'SoMa Seekers' roommate group.",
      time: "5 hours ago",
      isRead: true,
      icon: UserPlus
    },
    {
      id: 4,
      type: 'system',
      title: "Weekly AI Digest",
      message: "Our AI found 12 new properties in Mission District that match your budget.",
      time: "Yesterday",
      isRead: true,
      icon: Info
    }
  ]);

  useEffect(() => {
    setTimeout(() => setIsLoading(false), 800);
  }, []);

  const markAsRead = (id) => {
    setNotifications(prev => prev.map(n => n.id === id ? { ...n, isRead: true } : n));
  };

  const deleteNotification = (e, id) => {
    e.stopPropagation();
    setNotifications(prev => prev.filter(n => n.id !== id));
  };

  const clearAll = () => {
    setNotifications([]);
  };

  return (
    <div className="bg-slate-50 min-h-screen pb-24">
      {/* Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200 sticky top-0 z-40 flex items-center justify-between">
        <div className="flex items-center gap-4">
          <button 
            onClick={() => navigate(-1)}
            className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
          >
            <ArrowLeft size={20} className="text-black" />
          </button>
          <h1 className="text-lg font-bold text-black">Notifications</h1>
        </div>
        <div className="flex gap-2">
          {notifications.length > 0 && (
            <button 
              onClick={clearAll}
              className="text-[10px] font-black text-slate-400 uppercase tracking-widest hover:text-red-500 transition-colors"
            >
              Clear All
            </button>
          )}
          <button 
            onClick={() => navigate('/account-settings')}
            className="w-10 h-10 bg-slate-50 border border-slate-100 rounded-full flex items-center justify-center text-slate-400 hover:text-primary-500 transition-colors"
          >
            <Settings size={18} />
          </button>
        </div>
      </div>

      <div className="max-w-2xl mx-auto p-6">
        {isLoading ? (
          <div className="flex flex-col items-center justify-center py-32 space-y-4">
             <div className="w-12 h-12 border-4 border-primary-500 border-t-transparent rounded-full animate-spin" />
             <p className="font-bold text-slate-400 italic">Syncing updates...</p>
          </div>
        ) : notifications.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-32 text-center space-y-6">
             <div className="w-24 h-24 bg-white rounded-[40px] flex items-center justify-center text-slate-100 shadow-sm border border-slate-100">
                <Bell size={48} />
             </div>
             <div>
               <h3 className="text-2xl font-black text-black font-heading mb-2">All Caught Up!</h3>
               <p className="text-slate-400 font-medium max-w-xs mx-auto">You don't have any new notifications right now. Go live your life!</p>
             </div>
             <button 
              onClick={() => navigate('/home')}
              className="bg-black text-white px-10 py-4 rounded-3xl font-black text-sm uppercase tracking-widest shadow-xl transition-all hover:scale-105"
             >
              Back to Dashboard
             </button>
          </div>
        ) : (
          <div className="space-y-4">
            <AnimatePresence mode="popLayout">
              {notifications.map((notif) => (
                <motion.div
                  key={notif.id}
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, scale: 0.95 }}
                  onClick={() => markAsRead(notif.id)}
                  className={`bg-white p-6 rounded-[32px] shadow-sm border border-slate-100 relative group cursor-pointer transition-all hover:shadow-md hover:border-primary-100 ${!notif.isRead ? 'border-primary-100' : ''}`}
                >
                  {!notif.isRead && (
                    <div className="absolute top-6 left-2 w-1.5 h-1.5 bg-primary-600 rounded-full shadow-lg shadow-primary-500/50" />
                  )}
                  
                  <div className="flex gap-4">
                    <div className={`w-12 h-12 rounded-2xl flex items-center justify-center shrink-0 shadow-sm ${
                      notif.type === 'match' ? 'bg-amber-50 text-amber-500 border border-amber-100' :
                      notif.type === 'tour' ? 'bg-green-50 text-green-500 border border-green-100' :
                      notif.type === 'group' ? 'bg-primary-50 text-primary-600 border border-primary-100' :
                      'bg-slate-50 text-slate-400 border border-slate-100'
                    }`}>
                      <notif.icon size={24} />
                    </div>
                    
                    <div className="flex-1 min-w-0">
                      <div className="flex justify-between items-start mb-1">
                        <h4 className={`text-sm font-black text-black pr-6 ${!notif.isRead ? 'text-primary-900' : 'text-slate-700'}`}>
                          {notif.title}
                        </h4>
                        <span className="text-[9px] font-black text-slate-400 uppercase tracking-widest shrink-0 mt-0.5">
                          {notif.time}
                        </span>
                      </div>
                      <p className="text-xs text-slate-500 font-medium leading-relaxed mb-4">
                        {notif.message}
                      </p>
                      
                      <div className="flex items-center gap-3">
                         <button className="text-[10px] font-black text-primary-600 uppercase tracking-wider hover:underline">View Details</button>
                         <div className="w-1 h-1 bg-slate-200 rounded-full" />
                         <button 
                          onClick={(e) => deleteNotification(e, notif.id)}
                          className="text-[10px] font-black text-slate-300 uppercase tracking-wider hover:text-red-500 transition-colors"
                         >
                          Dismiss
                         </button>
                      </div>
                    </div>
                  </div>
                </motion.div>
              ))}
            </AnimatePresence>
          </div>
        )}

        {!isLoading && notifications.length > 0 && (
          <div className="mt-12 text-center pb-12">
            <div className="inline-flex items-center gap-2 px-4 py-2 bg-slate-100 rounded-full">
              <Sparkles size={12} className="text-primary-500" />
              <span className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Powered by Roomshare AI</span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default NotificationsScreen;

import React from 'react';
import { motion } from 'framer-motion';
import { Settings, Shield, Bell, Key, CreditCard, ChevronRight, ArrowLeft } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';

const SettingsScreen = () => {
  const navigate = useNavigate();

  const settingsGroups = [
    {
      title: 'Account',
      items: [
        { icon: Settings, label: 'Personal Information', color: 'text-blue-700', bg: 'bg-blue-50' },
        { icon: Key, label: 'Password & Security', color: 'text-purple-700', bg: 'bg-purple-50' },
        { icon: CreditCard, label: 'Payment Methods', color: 'text-green-700', bg: 'bg-green-50' },
      ]
    },
    {
      title: 'Preferences',
      items: [
        { icon: Bell, label: 'Notifications', color: 'text-amber-700', bg: 'bg-amber-50' },
        { icon: Shield, label: 'Privacy & Sharing', color: 'text-red-700', bg: 'bg-red-50' },
      ]
    }
  ];

  return (
    <div className="p-4 md:p-8 pb-24 md:pb-12 min-h-screen">
      <div className="max-w-3xl mx-auto">
        <div className="flex items-center gap-4 mb-8">
          <Link to={-1} className="glass-panel p-3 rounded-xl hover:bg-slate-100 transition-colors border border-slate-200">
            <ArrowLeft size={20} className="text-black" />
          </Link>
          <h1 className="text-3xl font-bold font-heading text-black">Settings</h1>
        </div>

        {settingsGroups.map((group, i) => (
          <motion.div 
            key={group.title}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.1 }}
            className="mb-8"
          >
            <h2 className="text-lg font-bold text-slate-600 mb-4 px-2 uppercase tracking-wider">{group.title}</h2>
            <div className="glass-panel rounded-3xl overflow-hidden divide-y divide-slate-100 border border-slate-200 shadow-xl">
              {group.items.map((item, j) => (
                <div key={j} className="flex items-center justify-between p-6 hover:bg-slate-50 cursor-pointer transition-all group">
                  <div className="flex items-center gap-4">
                    <div className={`p-4 rounded-xl ${item.bg} ${item.color} group-hover:scale-110 transition-transform shadow-sm border border-black/5`}>
                      <item.icon size={22} />
                    </div>
                    <span className="font-bold text-xl text-black">{item.label}</span>
                  </div>
                  <ChevronRight className="text-primary-600 group-hover:translate-x-1 transition-all" />
                </div>
              ))}
            </div>
          </motion.div>
        ))}

        <div className="mt-12 text-center text-sm text-slate-500 font-medium">
          <p>Roomshare AI version 1.0.0</p>
          <p className="mt-2 flex items-center justify-center gap-4">
            <a href="#" className="hover:text-black transition-colors">Terms of Service</a>
            <a href="#" className="hover:text-black transition-colors">Privacy Policy</a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default SettingsScreen;

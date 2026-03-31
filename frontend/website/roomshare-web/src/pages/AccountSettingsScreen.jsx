import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, Bell, Globe, Shield, 
  Mail, Lock, Trash2, ChevronRight, 
  CheckCircle2, AlertCircle 
} from 'lucide-react';

const AccountSettingsScreen = () => {
  const navigate = useNavigate();
  const [notifications, setNotifications] = useState(true);
  const [language, setLanguage] = useState('English (US)');
  const [privacy, setPrivacy] = useState('Public');

  const SettingGroup = ({ title, children }) => (
    <div className="space-y-4">
      <h3 className="text-sm font-black text-slate-400 uppercase tracking-widest px-2">{title}</h3>
      <div className="bg-white rounded-[32px] border border-slate-200 shadow-sm overflow-hidden">
        {children}
      </div>
    </div>
  );

  const SettingItem = ({ icon: Icon, title, value, onClick, color = "text-primary-600", bg = "bg-primary-50", showChevron = true }) => (
    <button 
      onClick={onClick}
      className="w-full flex items-center gap-4 p-5 hover:bg-slate-50 transition-colors border-b border-slate-50 last:border-0 group text-left"
    >
      <div className={`w-10 h-10 ${bg} ${color} rounded-xl flex items-center justify-center shrink-0`}>
        <Icon size={20} />
      </div>
      <div className="flex-1">
        <p className="font-bold text-black group-hover:text-primary-600 transition-colors">{title}</p>
        {value && <p className="text-xs text-slate-400 font-medium">{value}</p>}
      </div>
      {showChevron && <ChevronRight size={18} className="text-slate-300 group-hover:text-primary-600 transition-colors" />}
    </button>
  );

  const SwitchItem = ({ icon: Icon, title, checked, onChange, color = "text-primary-600", bg = "bg-primary-50" }) => (
    <div className="w-full flex items-center gap-4 p-5 border-b border-slate-50 last:border-0">
      <div className={`w-10 h-10 ${bg} ${color} rounded-xl flex items-center justify-center shrink-0`}>
        <Icon size={20} />
      </div>
      <div className="flex-1">
        <p className="font-bold text-black">{title}</p>
      </div>
      <button 
        onClick={() => onChange(!checked)}
        className={`w-12 h-6 rounded-full transition-all relative ${checked ? 'bg-primary-600' : 'bg-slate-200'}`}
      >
        <motion.div 
          animate={{ x: checked ? 26 : 2 }}
          className="absolute top-1 w-4 h-4 bg-white rounded-full shadow-sm"
        />
      </button>
    </div>
  );

  return (
    <div className="bg-slate-50 min-h-screen pb-24">
      {/* Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200 sticky top-0 z-40 flex items-center justify-between">
        <button 
          onClick={() => navigate(-1)}
          className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
        >
          <ArrowLeft size={20} className="text-black" />
        </button>
        <h1 className="text-lg font-bold text-black">Account Settings</h1>
        <div className="w-10" />
      </div>

      <div className="max-w-2xl mx-auto p-6 space-y-8">
        <SettingGroup title="General Settings">
          <SwitchItem 
            icon={Bell} 
            title="Push Notifications" 
            checked={notifications} 
            onChange={setNotifications} 
          />
          <SettingItem 
            icon={Globe} 
            title="Language" 
            value={language} 
            onClick={() => {}} 
          />
          <SettingItem 
            icon={Shield} 
            title="Privacy Settings" 
            value={privacy} 
            onClick={() => {}} 
          />
        </SettingGroup>

        <SettingGroup title="Account Security">
          <SettingItem 
            icon={Mail} 
            title="Change Email" 
            value="john@example.com" 
            onClick={() => {}} 
          />
          <SettingItem 
            icon={Lock} 
            title="Update Password" 
            value="••••••••••••" 
            onClick={() => {}} 
          />
        </SettingGroup>

        <SettingGroup title="Danger Zone">
          <SettingItem 
            icon={Trash2} 
            title="Delete Account" 
            value="Permanently remove your data" 
            color="text-red-500" 
            bg="bg-red-50" 
            onClick={() => {}} 
          />
        </SettingGroup>

        <div className="text-center pt-8">
          <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2">Roomshare AI v1.0.4</p>
          <p className="text-xs text-slate-400 font-medium">All your settings are synced across devices.</p>
        </div>
      </div>
    </div>
  );
};

export default AccountSettingsScreen;

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, Camera, User, 
  MapPin, Briefcase, Calendar, 
  Sparkles, CheckCircle2, Save,
  X 
} from 'lucide-react';

const EditProfileScreen = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    fullName: "Mike Chen",
    bio: "I'm a software engineer at a tech startup. Super clean, organized, and I usually spend my weekends hiking or exploring local coffee shops.",
    occupation: "Software Engineer",
    targetArea: "Pacific Heights, SF",
    moveInDate: "2024-11-01",
    budget: "$1,800/mo"
  });

  const [isLoading, setIsLoading] = useState(false);

  const handleSave = () => {
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
      navigate(-1);
    }, 1200);
  };

  const InputField = ({ label, icon: Icon, value, onChange, type = "text", placeholder }) => (
    <div className="space-y-2">
      <label className="text-xs font-black text-slate-400 uppercase tracking-widest ml-2">{label}</label>
      <div className="relative group">
        <div className="absolute left-5 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-primary-500 transition-colors">
          <Icon size={18} />
        </div>
        <input 
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          className="w-full bg-white border-2 border-slate-100 rounded-3xl py-4 flex items-center px-14 font-bold text-black focus:border-primary-500 focus:outline-none transition-all shadow-sm"
        />
      </div>
    </div>
  );

  return (
    <div className="bg-slate-50 min-h-screen pb-32">
      {/* Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200 sticky top-0 z-40 flex items-center justify-between">
        <button 
          onClick={() => navigate(-1)}
          className="w-10 h-10 bg-slate-100 rounded-full flex items-center justify-center hover:bg-slate-200 transition-colors"
        >
          <X size={20} className="text-black" />
        </button>
        <h1 className="text-lg font-bold text-black">Edit Profile</h1>
        <button 
          onClick={handleSave}
          disabled={isLoading}
          className="text-primary-600 font-black text-sm uppercase tracking-widest hover:text-primary-700 transition-colors disabled:opacity-50"
        >
          {isLoading ? "Saving..." : "Save"}
        </button>
      </div>

      <div className="max-w-2xl mx-auto p-6 space-y-12 pt-8">
        {/* Profile Picture Upload */}
        <div className="flex flex-col items-center">
          <div className="relative">
            <div className="w-32 h-32 rounded-[48px] bg-slate-200 overflow-hidden border-4 border-white shadow-2xl relative group cursor-pointer">
              <img 
                src="https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&q=80&w=400" 
                alt="Profile" 
                className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
              />
              <div className="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                <Camera size={24} className="text-white" />
              </div>
            </div>
            <div className="absolute -bottom-2 -right-2 w-10 h-10 bg-primary-600 rounded-2xl flex items-center justify-center text-white shadow-lg border-2 border-white">
              <Camera size={18} />
            </div>
          </div>
          <p className="mt-4 text-[10px] font-black text-slate-400 uppercase tracking-widest">Tap to change photo</p>
        </div>

        {/* Inputs */}
        <div className="space-y-6">
          <InputField 
            label="Full Name" 
            icon={User} 
            value={formData.fullName} 
            onChange={(e) => setFormData({...formData, fullName: e.target.value})} 
          />
          
          <div className="space-y-2">
            <label className="text-xs font-black text-slate-400 uppercase tracking-widest ml-2">Bio</label>
            <textarea 
              value={formData.bio}
              onChange={(e) => setFormData({...formData, bio: e.target.value})}
              rows={4}
              className="w-full bg-white border-2 border-slate-100 rounded-[32px] p-6 font-medium text-black focus:border-primary-500 focus:outline-none transition-all shadow-sm leading-relaxed"
            />
          </div>

          <InputField 
            label="Occupation" 
            icon={Briefcase} 
            value={formData.occupation} 
            onChange={(e) => setFormData({...formData, occupation: e.target.value})} 
          />

          <InputField 
            label="Preferred Area" 
            icon={MapPin} 
            value={formData.targetArea} 
            onChange={(e) => setFormData({...formData, targetArea: e.target.value})} 
          />

          <div className="grid grid-cols-2 gap-4">
            <InputField 
              label="Move-in" 
              icon={Calendar} 
              type="date"
              value={formData.moveInDate} 
              onChange={(e) => setFormData({...formData, moveInDate: e.target.value})} 
            />
            <InputField 
              label="Budget" 
              icon={Sparkles} 
              value={formData.budget} 
              onChange={(e) => setFormData({...formData, budget: e.target.value})} 
            />
          </div>
        </div>

        {/* AI Insight Placeholder */}
        <div className="bg-primary-50 border border-primary-100 p-8 rounded-[40px] flex gap-4 items-start">
          <div className="w-10 h-10 bg-white rounded-xl flex items-center justify-center text-primary-600 shadow-sm shrink-0">
            <CheckCircle2 size={20} />
          </div>
          <div>
            <h4 className="font-bold text-primary-900 mb-1">Roomshare AI Suggestion</h4>
            <p className="text-sm text-primary-800 leading-relaxed font-medium">
              Updating your bio to include more about your "Night Owl" lifestyle will improve matching accuracy by 24% for the Pacific Heights area.
            </p>
          </div>
        </div>
      </div>

      {/* Save Button */}
      <div className="fixed bottom-0 left-0 w-full p-6 z-40 bg-gradient-to-t from-slate-50 to-transparent">
        <div className="max-w-2xl mx-auto">
          <button 
            onClick={handleSave}
            disabled={isLoading}
            className="w-full bg-primary-600 hover:bg-primary-700 text-white p-5 rounded-3xl font-black shadow-2xl flex items-center justify-center gap-3 transition-all hover:scale-[1.02] disabled:opacity-70"
          >
            {isLoading ? <motion.div animate={{ rotate: 360 }} transition={{ repeat: Infinity, duration: 1 }} className="w-5 h-5 border-2 border-white border-t-transparent rounded-full" /> : (
              <>
                <Save size={20} />
                Save Changes
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
};

export default EditProfileScreen;

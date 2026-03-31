import React, { useState } from 'react';
import { Camera, MapPin, DollarSign, Home, ArrowRight, Loader2, CheckCircle2 } from 'lucide-react';
import { useNavigate, Link } from 'react-router-dom';
import { motion } from 'framer-motion';

const ListRoomScreen = () => {
  const [formData, setFormData] = useState({
    title: '',
    location: '',
    price: '',
    description: '',
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.title || !formData.price || !formData.location) return;
    
    setIsSubmitting(true);
    setTimeout(() => {
      setIsSubmitting(false);
      setIsSuccess(true);
      setTimeout(() => navigate('/home'), 2500);
    }, 2000);
  };

  const handleChange = (e) => setFormData({...formData, [e.target.name]: e.target.value});

  if (isSuccess) {
    return (
      <div className="min-h-screen flex items-center justify-center p-6">
        <motion.div 
          initial={{ opacity: 0, scale: 0.5 }}
          animate={{ opacity: 1, scale: 1 }}
          className="glass-panel p-10 rounded-3xl flex flex-col items-center text-center max-w-md w-full border border-slate-200 shadow-xl"
        >
          <div className="w-20 h-20 bg-green-50 border border-green-200 rounded-full flex items-center justify-center mb-6">
            <CheckCircle2 size={40} className="text-green-600" />
          </div>
          <h1 className="text-3xl font-bold mb-2 text-black">Room Listed!</h1>
          <p className="text-slate-600 font-bold">Your room is now live and waiting for AI matches.</p>
        </motion.div>
      </div>
    );
  }

  return (
    <div className="p-4 md:p-8 flex items-center justify-center min-h-screen">
      <div className="max-w-2xl w-full">
        <div className="flex items-center gap-4 mb-8">
          <Link to={-1} className="glass-panel p-3 rounded-xl hover:bg-slate-100 transition-colors border border-slate-200 shadow-sm">
            <ArrowRight size={20} className="text-black rotate-180" />
          </Link>
          <h1 className="text-3xl font-bold font-heading text-black">List a Room</h1>
        </div>

        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="glass-panel p-6 md:p-10 rounded-3xl relative overflow-hidden border border-slate-200 shadow-2xl"
        >
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="text-sm font-bold text-slate-700 block mb-2">Listing Title</label>
              <div className="relative">
                <Home size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                <input 
                  name="title"
                  type="text" 
                  value={formData.title}
                  onChange={handleChange}
                  required
                  placeholder="e.g. Beautiful Master Bedroom in Downtown"
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 pl-12 pr-4 text-black font-bold outline-none focus:border-primary-500/50 focus:bg-white transition-all shadow-inner"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="text-sm font-bold text-slate-700 block mb-2">Location</label>
                <div className="relative">
                  <MapPin size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                  <input 
                    name="location"
                    type="text" 
                    value={formData.location}
                    onChange={handleChange}
                    required
                    placeholder="e.g. Manhattan, NY"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 pl-12 pr-4 text-black font-bold outline-none focus:border-primary-500/50 focus:bg-white transition-all shadow-inner"
                  />
                </div>
              </div>
              <div>
                <label className="text-sm font-bold text-slate-700 block mb-2">Monthly Rent</label>
                <div className="relative">
                  <DollarSign size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-primary-600" />
                  <input 
                    name="price"
                    type="number" 
                    value={formData.price}
                    onChange={handleChange}
                    required
                    placeholder="1200"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl py-3 pl-12 pr-4 text-black font-bold outline-none focus:border-primary-500/50 focus:bg-white transition-all shadow-inner"
                  />
                </div>
              </div>
            </div>

            <div>
              <label className="text-sm font-bold text-slate-700 block mb-2">Description</label>
              <textarea 
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Tell us about the room, amenities, and ideal roommate..."
                rows="4"
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-4 text-black font-bold outline-none focus:border-primary-500/50 focus:bg-white transition-all shadow-inner resize-none"
              />
            </div>

            <div className="p-8 border-2 border-dashed border-slate-300 rounded-2xl flex flex-col items-center justify-center text-slate-500 hover:border-primary-500/50 hover:bg-slate-50 transition-colors cursor-pointer">
              <Camera size={32} className="mb-2" />
              <p className="font-bold text-black mb-1">Upload Photos</p>
              <p className="text-xs font-bold text-slate-400">Drag and drop or click to browse</p>
            </div>

            <button 
              type="submit" 
              disabled={isSubmitting}
              className="w-full bg-black hover:bg-slate-800 text-white font-bold py-4 rounded-xl flex items-center justify-center gap-2 transition-all hover:scale-[1.02] shadow-xl mt-4 disabled:opacity-70 disabled:hover:scale-100"
            >
              {isSubmitting ? (
                <Loader2 size={20} className="animate-spin" />
              ) : (
                <React.Fragment>
                  Publish Listing
                  <ArrowRight size={18} />
                </React.Fragment>
              )}
            </button>
          </form>
        </motion.div>
      </div>
    </div>
  );
};

export default ListRoomScreen;
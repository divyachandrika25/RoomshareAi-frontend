import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { motion } from 'framer-motion';
import { 
  ArrowLeft, Calendar, Briefcase, 
  Clock, CheckCircle2, ShieldCheck,
  ChevronRight, Info
} from 'lucide-react';

const BookingDetailsScreen = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [formData, setFormData] = useState({
    moveInDate: '',
    duration: '',
    employment: ''
  });
  const [showProgress, setShowProgress] = useState(true);

  const durationOptions = ["3 Months", "6 Months", "12 Months", "24+ Months"];
  const employmentOptions = ["Full-time", "Part-time", "Student", "Unemployed"];

  const isFormValid = formData.moveInDate && formData.duration && formData.employment;

  return (
    <div className="bg-slate-50 min-h-screen flex flex-col">
      {/* Header */}
      <div className="bg-white px-6 py-6 border-b border-slate-200">
        <div className="flex items-center justify-between mb-8">
           <button 
            onClick={() => navigate(-1)}
            className="w-10 h-10 bg-slate-50 rounded-full flex items-center justify-center hover:bg-slate-100 transition-colors"
          >
            <ArrowLeft size={20} className="text-black" />
          </button>
          <h1 className="text-lg font-black text-black uppercase tracking-widest">Available Rooms</h1>
          <div className="w-10" />
        </div>

        {/* Progress Bar */}
        <div className="flex gap-2 max-w-xs mx-auto mb-2">
           {[1, 2, 3, 4].map((step) => (
             <div 
              key={step} 
              className={`h-1.5 flex-1 rounded-full transition-all duration-500 ${step <= 2 ? 'bg-primary-600' : 'bg-slate-100'}`} 
             />
           ))}
        </div>
      </div>

      <div className="flex-1 overflow-y-auto p-8 max-w-2xl mx-auto w-full">
         <motion.div
           initial={{ opacity: 0, y: 20 }}
           animate={{ opacity: 1, y: 0 }}
           className="space-y-12"
         >
            <div>
               <h2 className="text-4xl font-black text-black font-heading mb-4">Your Details</h2>
               <p className="text-slate-400 font-bold leading-relaxed">
                  Help the host understand your requirements before we proceed to AI trust verification.
               </p>
            </div>

            <div className="space-y-8">
               {/* Move-in Date */}
               <div className="space-y-4">
                  <label className="text-[10px] font-black text-slate-300 uppercase tracking-[0.2em] px-1">Preferred Move-in Date</label>
                  <div className="relative group">
                     <Calendar className="absolute left-6 top-1/2 -translate-y-1/2 text-primary-500 group-hover:scale-110 transition-transform" size={20} />
                     <input 
                      type="date"
                      value={formData.moveInDate}
                      onChange={(e) => setFormData({...formData, moveInDate: e.target.value})}
                      className="w-full bg-white border border-slate-100 rounded-[24px] py-5 pl-16 pr-8 font-black text-black focus:border-primary-500 focus:outline-none focus:ring-4 focus:ring-primary-500/5 transition-all shadow-sm"
                     />
                  </div>
               </div>

               {/* Duration */}
               <div className="space-y-4">
                  <label className="text-[10px] font-black text-slate-300 uppercase tracking-[0.2em] px-1">Duration of Stay</label>
                  <div className="grid grid-cols-2 gap-3">
                     {durationOptions.map(opt => (
                        <button
                          key={opt}
                          onClick={() => setFormData({...formData, duration: opt})}
                          className={`p-4 rounded-2xl border-2 font-black text-xs uppercase tracking-widest transition-all ${
                            formData.duration === opt 
                              ? 'bg-primary-600 border-primary-600 text-white shadow-xl shadow-primary-500/20 scale-[1.02]' 
                              : 'bg-white border-slate-50 text-slate-400 hover:border-primary-100'
                          }`}
                        >
                          {opt}
                        </button>
                     ))}
                  </div>
               </div>

               {/* Employment */}
               <div className="space-y-4">
                  <label className="text-[10px] font-black text-slate-300 uppercase tracking-[0.2em] px-1">Employment Status</label>
                  <div className="space-y-3">
                     {employmentOptions.map(opt => (
                        <button
                          key={opt}
                          onClick={() => setFormData({...formData, employment: opt})}
                          className={`w-full flex items-center justify-between p-5 rounded-[24px] border-2 transition-all ${
                            formData.employment === opt 
                              ? 'bg-primary-50 border-primary-500 text-primary-900 group shadow-sm' 
                              : 'bg-white border-slate-50 text-slate-500 hover:border-slate-100'
                          }`}
                        >
                          <div className="flex items-center gap-4">
                             <div className={`w-10 h-10 rounded-xl flex items-center justify-center ${formData.employment === opt ? 'bg-primary-600 text-white' : 'bg-slate-50 text-slate-400'}`}>
                                <Briefcase size={20} />
                             </div>
                             <span className="font-black text-xs uppercase tracking-widest">{opt}</span>
                          </div>
                          {formData.employment === opt && <CheckCircle2 size={20} className="text-primary-600" />}
                        </button>
                     ))}
                  </div>
               </div>
            </div>
         </motion.div>
      </div>

      {/* Sticky Bottom Button */}
      <div className="p-8 bg-white/80 backdrop-blur-xl border-t border-slate-100">
         <button
           disabled={!isFormValid}
           onClick={() => navigate('/safety-verification')}
           className={`w-full py-6 rounded-[28px] font-black text-sm uppercase tracking-[0.2em] shadow-2xl transition-all flex items-center justify-center gap-3 ${
             isFormValid 
               ? 'bg-black text-white hover:scale-[1.02] active:scale-95 shadow-black/20' 
               : 'bg-slate-100 text-slate-300 cursor-not-allowed'
           }`}
         >
           Continue to Verification
           <ArrowLeft size={18} className="rotate-180" />
         </button>
         
         <div className="mt-4 flex items-center justify-center gap-2">
            <ShieldCheck size={14} className="text-primary-500" />
            <span className="text-[9px] font-black text-slate-400 uppercase tracking-widest">Your data is secured by NeuralTrust AI</span>
         </div>
      </div>
    </div>
  );
};

export default BookingDetailsScreen;

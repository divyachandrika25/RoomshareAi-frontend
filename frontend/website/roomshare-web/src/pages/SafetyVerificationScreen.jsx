import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ShieldCheck, Lock, Upload, 
  CheckCircle2, AlertTriangle, Info,
  ArrowRight, Loader2, Sparkles,
  Camera, Fingerprint, Eye
} from 'lucide-react';

const SafetyVerificationScreen = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(1);
  const [isProcessing, setIsProcessing] = useState(false);
  const [files, setFiles] = useState([]);

  const handleUpload = () => {
    setIsProcessing(true);
    // Simulate AI scanning
    setTimeout(() => {
      setIsProcessing(false);
      setStep(3);
    }, 3000);
  };

  const steps = [
    { title: "ID Verification", desc: "Upload a photo of your government-issued ID." },
    { title: "AI Liveness Check", desc: "Scan your face to confirm identity ownership." },
    { title: "Neural Profile Scan", desc: "AI is analyzing your public footprint." }
  ];

  return (
    <div className="bg-slate-900 min-h-screen text-white flex flex-col p-8 overflow-hidden">
       {/* Background Aesthetics */}
       <div className="absolute top-0 left-0 w-full h-full pointer-events-none opacity-20">
          <div className="absolute top-[-10%] left-[-10%] w-[50%] h-[50%] bg-primary-600 rounded-full blur-[120px] animate-pulse" />
          <div className="absolute bottom-[-10%] right-[-10%] w-[50%] h-[50%] bg-indigo-600 rounded-full blur-[120px] animate-pulse ring-offset-4" />
       </div>

       {/* Header */}
       <div className="relative z-10 mb-12 flex justify-between items-center">
          <div className="w-12 h-12 bg-white/10 backdrop-blur-xl border border-white/10 rounded-2xl flex items-center justify-center">
             <ShieldCheck size={28} className="text-primary-400" />
          </div>
          <div className="flex bg-white/5 border border-white/5 rounded-full px-4 py-2 gap-3">
             {steps.map((_, i) => (
                <div 
                  key={i} 
                  className={`w-2 h-2 rounded-full transition-all duration-500 ${step > i ? 'bg-primary-500 scale-125' : 'bg-white/20'}`} 
                />
             ))}
          </div>
       </div>

       <div className="relative z-10 flex-1 flex flex-col justify-center max-w-lg mx-auto w-full">
          <AnimatePresence mode="wait">
             {step === 1 && (
                <motion.div
                  key="step1"
                  initial={{ opacity: 0, x: 20 }}
                  animate={{ opacity: 1, x: 0 }}
                  exit={{ opacity: 0, x: -20 }}
                  className="space-y-10"
                >
                   <div>
                      <h1 className="text-5xl font-black font-heading mb-4 tracking-tight">Identity First.</h1>
                      <p className="text-slate-400 font-bold leading-relaxed text-lg">
                        To maintain our 99% safety rating, we need to verify your official documentation. 
                        Your data is <span className="text-primary-400">fully encrypted</span> and never shared.
                      </p>
                   </div>

                   <div 
                    onClick={() => setStep(2)}
                    className="group border-2 border-dashed border-white/20 hover:border-primary-500/50 hover:bg-white/5 p-12 rounded-[40px] flex flex-col items-center justify-center transition-all cursor-pointer text-center"
                   >
                      <div className="w-20 h-20 bg-primary-600 rounded-[32px] flex items-center justify-center mb-6 shadow-2xl shadow-primary-500/40 group-hover:scale-110 transition-transform">
                         <Camera size={36} />
                      </div>
                      <h4 className="text-xl font-black mb-2 uppercase tracking-tight">Secure ID Scan</h4>
                      <p className="text-[10px] font-black text-slate-500 uppercase tracking-widest">Supports Passport, Driver License, or National ID</p>
                   </div>
                </motion.div>
             )}

             {step === 2 && !isProcessing && (
                <motion.div
                  key="step2"
                  initial={{ opacity: 0, scale: 0.9 }}
                  animate={{ opacity: 1, scale: 1 }}
                  className="space-y-10 flex flex-col items-center text-center"
                >
                    <div className="w-48 h-48 rounded-full border-4 border-primary-500 flex items-center justify-center p-2 relative">
                       <div className="absolute inset-0 bg-primary-500/20 rounded-full animate-ping" />
                       <div className="w-full h-full rounded-full bg-slate-800 flex items-center justify-center shadow-inner">
                          <Eye size={64} className="text-primary-400" />
                       </div>
                    </div>
                    <div>
                       <h2 className="text-3xl font-black uppercase tracking-tight mb-2">Almost There</h2>
                       <p className="text-slate-400 font-bold">Please ensure your face is clearly visible in the frame.</p>
                    </div>
                    <button 
                      onClick={handleUpload}
                      className="w-full bg-white text-black py-6 rounded-[28px] font-black uppercase text-sm tracking-[0.2em] shadow-2xl hover:scale-105 active:scale-95 transition-all"
                    >
                      Process Neural ID
                    </button>
                </motion.div>
             )}

             {isProcessing && (
                <motion.div
                  key="processing"
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="flex flex-col items-center gap-8"
                >
                   <div className="relative">
                      <Loader2 size={80} className="text-primary-500 animate-spin" />
                      <div className="absolute inset-0 flex items-center justify-center">
                         <Sparkles size={24} className="text-white animate-pulse" />
                      </div>
                   </div>
                   <div className="text-center space-y-2">
                      <h3 className="text-2xl font-black uppercase tracking-widest animate-pulse">Neural Scanning...</h3>
                      <p className="text-xs font-bold text-slate-500 uppercase tracking-widest">Running cross-check through 24 database nodes</p>
                   </div>
                </motion.div>
             )}

             {step === 3 && (
                <motion.div
                  key="step3"
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="space-y-8 text-center"
                >
                   <div className="w-24 h-24 bg-green-500 rounded-[32px] flex items-center justify-center mx-auto mb-8 shadow-2xl shadow-green-500/30">
                      <CheckCircle2 size={48} strokeWidth={3} />
                   </div>
                   <div>
                      <h1 className="text-5xl font-black font-heading tracking-tight mb-4">Trust Established.</h1>
                      <p className="text-slate-400 font-bold leading-relaxed mb-10">
                        Your identity has been verified through our NeuralTrust system. 
                        Host transparency is now enabled for your account.
                      </p>
                   </div>
                   <button 
                    onClick={() => navigate('/booking-success')}
                    className="w-full bg-primary-600 py-6 rounded-[28px] font-black uppercase text-sm tracking-[0.2em] shadow-2xl hover:bg-primary-700 transition-all active:scale-95"
                   >
                     Finalize Booking
                   </button>
                </motion.div>
             )}
          </AnimatePresence>
       </div>

       {/* Footer Branding */}
       <div className="relative z-10 flex items-center justify-center gap-2 mt-auto">
          <Fingerprint size={16} className="text-slate-600" />
          <span className="text-[10px] font-black text-slate-600 uppercase tracking-[0.2em]">Secured by NeuralTrust Biometrics</span>
       </div>
    </div>
  );
};

export default SafetyVerificationScreen;

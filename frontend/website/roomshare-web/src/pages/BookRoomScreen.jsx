import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { ArrowLeft, CheckCircle2, CreditCard, Smartphone, Building2, CalendarClock } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';

const BookRoomScreen = () => {
  const [isBooked, setIsBooked] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState('card'); // card, upi, netbanking, emi
  const navigate = useNavigate();

  const handleBook = () => {
    setIsBooked(true);
    setTimeout(() => {
      navigate('/home');
    }, 3000);
  };

  if (isBooked) {
    return (
      <div className="min-h-[calc(100vh-80px)] md:min-h-screen flex items-center justify-center p-6">
        <motion.div 
          initial={{ opacity: 0, scale: 0.5 }}
          animate={{ opacity: 1, scale: 1 }}
          className="glass-panel p-10 rounded-3xl flex flex-col items-center text-center max-w-md w-full border border-slate-200"
        >
          <div className="w-20 h-20 bg-green-50 rounded-full flex items-center justify-center mb-6 border border-green-200">
            <CheckCircle2 size={40} className="text-green-600" />
          </div>
          <h1 className="text-3xl font-bold mb-2 text-black">Booking Requested!</h1>
          <p className="text-slate-600 font-medium">The host has been notified. You will redirect to home shortly.</p>
        </motion.div>
      </div>
    );
  }

  return (
    <div className="p-4 md:p-8 flex justify-center min-h-screen pb-24 md:pb-8">
      <div className="max-w-xl w-full">
        <div className="flex items-center gap-4 mb-8">
          <Link to={-1} className="glass-panel p-3 rounded-xl hover:bg-slate-100 transition-colors border border-slate-200">
            <ArrowLeft size={20} className="text-black" />
          </Link>
          <h1 className="text-3xl font-bold font-heading text-black">Complete Booking</h1>
        </div>

        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="glass-panel p-6 md:p-8 rounded-3xl relative overflow-hidden border border-slate-200"
        >
          {/* Subtle glow */}
          <div className="absolute -top-10 -right-10 w-40 h-40 bg-primary-500/5 blur-[60px] rounded-full pointer-events-none" />

          <h2 className="text-xl font-bold mb-6">Payment Method</h2>
          
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-3 mb-8">
             {[
               { id: 'card', icon: CreditCard, label: 'Card' },
               { id: 'upi', icon: Smartphone, label: 'UPI' },
               { id: 'netbanking', icon: Building2, label: 'NetBank' },
               { id: 'emi', icon: CalendarClock, label: 'EMI' },
             ].map(method => (
               <button 
                 key={method.id}
                 onClick={() => setPaymentMethod(method.id)}
                 className={`p-3 rounded-xl border flex flex-col items-center justify-center gap-2 transition-all ${
                   paymentMethod === method.id 
                    ? 'bg-primary-600 border-primary-700 text-white shadow-lg' 
                    : 'bg-slate-50 border-slate-200 text-slate-500 hover:bg-slate-100'
                 }`}
               >
                 <method.icon size={24} />
                 <span className="text-xs font-bold">{method.label}</span>
               </button>
             ))}
          </div>

          <div className="min-h-[180px] mb-8">
            <AnimatePresence mode="wait">
              {paymentMethod === 'card' && (
                <motion.div key="card" initial={{ opacity: 0, x: -10 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: 10 }} className="space-y-4">
                  <div className="bg-slate-50 border border-slate-200 rounded-xl px-4 py-3.5 focus-within:border-primary-500/50 focus:bg-white transition-all shadow-sm">
                    <label className="text-xs text-slate-700 font-bold block mb-1">Card Number</label>
                    <input type="text" placeholder="0000 0000 0000 0000" className="w-full bg-transparent outline-none text-black font-bold font-mono placeholder-slate-400" />
                  </div>
                  <div className="flex gap-4">
                    <div className="flex-1 bg-slate-50 border border-slate-200 rounded-xl px-4 py-3.5 focus-within:border-primary-500/50 focus:bg-white transition-all shadow-sm">
                      <label className="text-xs text-slate-700 font-bold block mb-1">Expiry</label>
                      <input type="text" placeholder="MM/YY" className="w-full bg-transparent outline-none text-black font-bold font-mono placeholder-slate-400" />
                    </div>
                    <div className="flex-1 bg-slate-50 border border-slate-200 rounded-xl px-4 py-3.5 focus-within:border-primary-500/50 focus:bg-white transition-all shadow-sm">
                      <label className="text-xs text-slate-700 font-bold block mb-1">CVC</label>
                      <input type="text" placeholder="123" className="w-full bg-transparent outline-none text-black font-bold font-mono placeholder-slate-400" />
                    </div>
                  </div>
                </motion.div>
              )}

              {paymentMethod === 'upi' && (
                <motion.div key="upi" initial={{ opacity: 0, x: -10 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: 10 }} className="space-y-4">
                  <div className="flex gap-3 mb-4">
                    {['Google Pay', 'PhonePe', 'Paytm'].map(app => (
                      <div key={app} className="flex-1 bg-slate-50 border border-slate-200 rounded-xl py-3 text-center text-sm font-bold hover:bg-slate-100 cursor-pointer text-slate-700">
                        {app}
                      </div>
                    ))}
                  </div>
                  <div className="bg-slate-50 border border-slate-200 rounded-xl px-4 py-3.5 focus-within:border-primary-500/50 focus:bg-white transition-all shadow-sm">
                    <label className="text-xs text-slate-700 font-bold block mb-1">Enter UPI ID</label>
                    <input type="text" placeholder="username@upi" className="w-full bg-transparent outline-none text-black font-bold placeholder-slate-400" />
                  </div>
                </motion.div>
              )}

              {paymentMethod === 'netbanking' && (
                <motion.div key="netbanking" initial={{ opacity: 0, x: -10 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: 10 }} className="space-y-4">
                  <div className="bg-slate-50 border border-slate-200 rounded-xl px-4 py-3 focus-within:border-primary-500/50 focus:bg-white transition-all shadow-sm">
                    <label className="text-xs text-slate-700 font-bold block mb-1">Select Bank</label>
                    <select className="w-full bg-transparent outline-none text-black font-bold appearance-none">
                      <option value="" disabled className="bg-white">Choose your bank...</option>
                      <option value="sbi" className="bg-white">State Bank of India</option>
                      <option value="hdfc" className="bg-white">HDFC Bank</option>
                      <option value="icici" className="bg-white">ICICI Bank</option>
                      <option value="axis" className="bg-white">Axis Bank</option>
                    </select>
                  </div>
                </motion.div>
              )}

              {paymentMethod === 'emi' && (
                <motion.div key="emi" initial={{ opacity: 0, x: -10 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: 10 }} className="space-y-4">
                   <div className="bg-slate-50 border border-slate-200 rounded-xl px-4 py-3 focus-within:border-primary-500/50 focus:bg-white transition-all shadow-sm mb-4">
                    <label className="text-xs text-slate-700 font-bold block mb-1">Select EMI Tenure</label>
                    <select className="w-full bg-transparent outline-none text-black font-bold appearance-none">
                      <option value="3" className="bg-white">3 Months - $416.66/mo (No Cost)</option>
                      <option value="6" className="bg-white">6 Months - $208.33/mo (No Cost)</option>
                      <option value="12" className="bg-white">12 Months - $104.16/mo</option>
                    </select>
                  </div>
                  <div className="bg-slate-50 border border-slate-200 rounded-xl px-4 py-3 focus-within:border-primary-500/50 focus:bg-white transition-all shadow-sm">
                    <label className="text-xs text-slate-700 font-bold block mb-1">Credit Card Number</label>
                    <input type="text" placeholder="0000 0000 0000 0000" className="w-full bg-transparent outline-none text-black font-bold font-mono" />
                  </div>
                </motion.div>
              )}
            </AnimatePresence>
          </div>

          <div className="border-t border-slate-200 pt-6 mb-8">
            <div className="flex justify-between text-slate-600 mb-2 font-medium">
              <span>Monthly Rent</span>
              <span>$1,200.00</span>
            </div>
            <div className="flex justify-between text-slate-600 mb-2 font-medium">
              <span>Platform Fee</span>
              <span>$50.00</span>
            </div>
            <div className="flex justify-between font-bold text-lg text-black mt-4 border-t border-slate-200 pt-4">
              <span>Total Due</span>
              <span>$1,250.00</span>
            </div>
          </div>

          <button 
            onClick={handleBook}
            className="w-full bg-primary-600 hover:bg-primary-500 text-white font-bold py-4 rounded-xl flex items-center justify-center transition-transform hover:scale-[1.02] shadow-lg shadow-primary-500/30"
          >
            Confirm Booking
          </button>
        </motion.div>
      </div>
    </div>
  );
};

export default BookRoomScreen;

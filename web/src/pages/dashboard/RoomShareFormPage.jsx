import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { roomAPI } from '@/lib/api';
import {
  ArrowLeft, Calendar, Clock, MessageSquare,
  ShieldCheck, CheckCircle2, ChevronRight, Home,
  Users, Briefcase, MapPin, Star, Sparkles,
  BadgeCheck, Building2, Send
} from 'lucide-react';
import toast from 'react-hot-toast';

export default function RoomShareFormPage() {
  const { roomId }  = useParams();
  const { user }    = useAuth();
  const navigate    = useNavigate();
  const email       = user?.email || '';

  const [loading,    setLoading]    = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [roomData,   setRoomData]   = useState(null);
  const [formData,   setFormData]   = useState({
    preferred_move_in_date: '',
    duration_of_stay:       '12 Months',
    intro_message:          '',
    employment_status:      'Full-time',
  });

  useEffect(() => {
    if (!roomId || !email) return;
    setLoading(true);
    roomAPI.getRoomShareForm(roomId, email)
      .then(res => {
        if (res.data?.data) {
          setRoomData(res.data.data);
          if (res.data.data.suggested_date)
            setFormData(p => ({ ...p, preferred_move_in_date: res.data.data.suggested_date }));
        }
      })
      .catch(err => { console.error(err); toast.error("Failed to load application form"); })
      .finally(() => setLoading(false));
  }, [roomId, email]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.preferred_move_in_date) return toast.error("Please select a move-in date");
    if (!formData.intro_message.trim())    return toast.error("Please write a message to the host");
    setSubmitting(true);
    try {
      const res = await roomAPI.submitRoomShareRequest({ room_id: roomId, user_email: email, ...formData });
      if (res.data?.success) {
        toast.success("Application submitted successfully!");
        const rid = res.data.data?.request_id;
        navigate(rid ? `/dashboard/room-share/final-review/${rid}` : '/dashboard/notifications');
      }
    } catch (err) {
      toast.error(err.response?.data?.error || "Submission failed");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <LoadingState />;

  const roomTitle = roomData?.room_title || 'Luxury Apartment';
  const roomRent  = roomData?.room_rent  || '₹0';
  const roomType  = roomData?.room_type  || 'Private Room';
  const roomImg   = roomData?.room_image || roomData?.owner_photo || 'https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?auto=format&fit=crop&q=80&w=800';
  const mates     = roomData?.mates_count || '2';
  const location  = roomData?.location   || 'Chennai';

  return (
    <div className="bg-black min-h-screen font-sans">
      {/* Custom Keyframes - Keeps component plug-and-play without editing tailwind.config.js */}
      <style>{`
        @keyframes fadeUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
        @keyframes scalePop { from { opacity: 0; transform: scale(0.92); } to { opacity: 1; transform: scale(1); } }
        .animate-fade-up { animation: fadeUp 0.55s ease both; }
        .animate-scale-pop { animation: scalePop 0.45s ease both; }
        .delay-50 { animation-delay: 50ms; }
        .delay-100 { animation-delay: 100ms; }
        .delay-200 { animation-delay: 200ms; }
        .delay-300 { animation-delay: 300ms; }
      `}</style>

      {/* ── Top Bar ── */}
      <TopBar onBack={() => navigate(-1)} />

      <div className="max-w-[1152px] mx-auto px-4 pb-16">
        {/* ── Hero ── */}
        <FormHero title={roomTitle} />

        {/* ── Step indicator ── */}
        <StepIndicator current={1} />

        {/* ── Main grid ── */}
        <div className="grid grid-cols-1 md:grid-cols-[1fr_420px] gap-5 mt-6 items-start">
          {/* LEFT — Form ── */}
          <ApplicationForm
            formData={formData}
            setFormData={setFormData}
            submitting={submitting}
            onSubmit={handleSubmit}
            durationOptions={roomData?.duration_options}
            employmentOptions={roomData?.employment_options}
          />

          {/* RIGHT — Room info + how it works ── */}
          <RightPanel
            roomImg={roomImg}
            roomTitle={roomTitle}
            roomRent={roomRent}
            roomType={roomType}
            location={location}
            mates={mates}
            ownerName={roomData?.owner_name}
            ownerPhoto={roomData?.owner_photo}
            ownerQuote={roomData?.intro_quote}
          />
        </div>
      </div>
    </div>
  );
}

/* ─── TOP BAR ─── */
function TopBar({ onBack }) {
  return (
    <div className="bg-slate-900/80 backdrop-blur-md border-b border-slate-800 sticky top-0 z-50">
      <div className="max-w-[1152px] mx-auto px-4 h-16 flex items-center justify-between">
        <button 
          onClick={onBack} 
          className="flex items-center gap-2 bg-slate-800 border-none rounded-xl px-3.5 py-2 cursor-pointer text-slate-200 font-bold text-[13px] hover:bg-slate-700 transition-colors"
        >
          <ArrowLeft size={16} /> Back
        </button>
        <div className="flex items-center gap-2 bg-slate-800/50 rounded-full px-3.5 py-1.5 border border-slate-700">
          <div className="w-[22px] h-[22px] rounded-full bg-gradient-to-br from-blue-500 via-blue-600 to-indigo-500 shadow-sm" />
          <span className="text-[13px] font-extrabold text-white">RoomShare AI</span>
        </div>
        <div className="w-20" /> {/* Spacer to center the logo */}
      </div>
    </div>
  );
}

/* ─── HERO ─── */
function FormHero({ title }) {
  return (
    <div className="animate-scale-pop bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 border border-slate-800 rounded-b-[28px] px-4 md:px-6 pt-11 pb-14 relative overflow-hidden -mb-9 shadow-2xl shadow-blue-500/10">
      <div className="absolute w-[240px] h-[240px] rounded-full bg-blue-500/10 blur-3xl -top-20 -left-20" />
      <div className="absolute w-[160px] h-[160px] rounded-full bg-indigo-500/10 blur-3xl -top-10 -right-12" />

      <div className="flex items-center gap-3.5 relative z-10">
        <div className="w-[52px] h-[52px] rounded-2xl bg-blue-500/20 border-2 border-blue-500/30 flex items-center justify-center shrink-0 shadow-lg shadow-blue-500/20">
          <Home size={22} className="text-blue-400" />
        </div>
        <div>
          <div className="flex items-center gap-2 mb-1">
            <span className="text-[10px] font-extrabold text-slate-400 uppercase tracking-widest">
              Room Share Application
            </span>
            <div className="bg-blue-500/20 border border-blue-500/30 rounded-lg px-2 py-0.5">
              <span className="text-[9px] font-black text-blue-400 tracking-wider">
                STEP 1 OF 3
              </span>
            </div>
          </div>
          <h1 className="text-[22px] font-black text-white tracking-tight leading-tight m-0 max-w-[480px]">
            Apply for: {title}
          </h1>
        </div>
      </div>
    </div>
  );
}

/* ─── STEP INDICATOR ─── */
function StepIndicator({ current }) {
  const steps = ['Application Details', 'ID Verification', 'Confirmation'];
  return (
    <div className="animate-fade-up delay-50 flex items-center bg-slate-900 border border-slate-800 rounded-[18px] px-5 py-3.5 gap-2 shadow-[0_4px_20px_rgba(0,0,0,.3)] relative z-10 max-w-[680px] mx-auto overflow-x-auto">
      {steps.map((s, i) => {
        const done   = i + 1 < current;
        const active = i + 1 === current;
        return (
          <div key={i} className={`flex items-center gap-1.5 ${i < steps.length - 1 ? 'flex-1' : 'flex-none'}`}>
            <div className="flex items-center gap-2 shrink-0">
              <div className={`
                w-7 h-7 rounded-lg flex items-center justify-center
                ${done ? 'bg-emerald-500' : active ? 'bg-blue-600' : 'bg-slate-800'}
              `}>
                {done
                  ? <CheckCircle2 size={14} className="text-white" />
                  : <span className={`text-[11px] font-extrabold ${active ? 'text-white' : 'text-slate-500'}`}>
                      {i + 1}
                    </span>
                }
              </div>
              <span className={`
                text-[11px] whitespace-nowrap
                ${active ? 'font-extrabold text-blue-400' : done ? 'font-semibold text-emerald-500' : 'font-semibold text-slate-500'}
              `}>
                {s}
              </span>
            </div>
            {i < steps.length - 1 && (
              <div className={`flex-1 h-0.5 rounded-full mx-2 ${done ? 'bg-emerald-500' : 'bg-slate-800'}`} />
            )}
          </div>
        );
      })}
    </div>
  );
}

/* ─── APPLICATION FORM ─── */
function ApplicationForm({ formData, setFormData, submitting, onSubmit, durationOptions, employmentOptions }) {
  const set = (key) => (e) => setFormData(p => ({ ...p, [key]: e.target.value }));

  const finalDurations = durationOptions || ['3 Months','6 Months','12 Months','18 Months','24+ Months'];
  const finalEmployment = employmentOptions || ['Full-time','Part-time','Student','Freelance','Unemployed'];

  // Tailwind classes for form elements
  const inputBase = "w-full rounded-[14px] border-[1.5px] border-slate-800 bg-slate-900/50 text-sm font-semibold text-white outline-none transition-all duration-200 focus:border-blue-500 focus:shadow-[0_0_0_3px_rgba(59,130,246,0.15)] placeholder:text-slate-600";

  return (
    <div className="animate-fade-up delay-100 bg-slate-900 rounded-[24px] border border-slate-800 shadow-2xl overflow-hidden hover:-translate-y-[1px] transition-all duration-300">
      {/* Card header */}
      <div className="bg-gradient-to-br from-slate-800 to-slate-900 px-6 pt-5 pb-9 relative overflow-hidden border-b border-slate-800">
        <div className="absolute w-[120px] h-[120px] rounded-full bg-blue-500/5 -top-8 -right-8" />
        <div className="flex items-center gap-2.5 relative z-10">
          <div className="w-9 h-9 bg-blue-500/20 border border-blue-500/30 rounded-xl flex items-center justify-center shadow-lg shadow-blue-500/10">
            <MessageSquare size={16} className="text-blue-400" />
          </div>
          <div>
            <p className="text-sm font-extrabold text-white m-0">Application Details</p>
            <p className="text-[11px] text-slate-400 m-0">Fill in your preferences below</p>
          </div>
        </div>
      </div>

      <form onSubmit={onSubmit} className="p-6 -mt-5 relative z-10 bg-slate-900">
        {/* Move-in date */}
        <FormGroup label="Preferred Move-in Date" icon={<Calendar size={13} className="text-blue-400" />}>
          <input
            type="date"
            className={`${inputBase} h-[52px] px-4 appearance-none`}
            style={{ colorScheme: 'dark' }}
            value={formData.preferred_move_in_date}
            onChange={set('preferred_move_in_date')}
            required
          />
        </FormGroup>

        {/* Duration + Employment */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3.5 mb-5">
          <FormGroup label="Duration of Stay" icon={<Clock size={13} className="text-blue-400" />} noMb>
            <select 
              className={`${inputBase} h-[52px] px-4 appearance-none cursor-pointer bg-[url("data:image/svg+xml,%3Csvg_xmlns='http://www.w3.org/2000/svg'_width='12'_height='8'_viewBox='0_0_12_8'%3E%3Cpath_d='M1_1l5_5_5-5'_stroke='%23475569'_stroke-width='1.5'_fill='none'_stroke-linecap='round'/%3E%3C/svg%3E")] bg-no-repeat bg-[position:right_14px_center] pr-10`}
              value={formData.duration_of_stay} 
              onChange={set('duration_of_stay')}
            >
              {finalDurations.map(v => (
                <option key={v} value={v} className="bg-slate-900 border-none">{v}</option>
              ))}
            </select>
          </FormGroup>
          <FormGroup label="Employment Status" icon={<Briefcase size={13} className="text-blue-400" />} noMb>
            <select 
              className={`${inputBase} h-[52px] px-4 appearance-none cursor-pointer bg-[url("data:image/svg+xml,%3Csvg_xmlns='http://www.w3.org/2000/svg'_width='12'_height='8'_viewBox='0_0_12_8'%3E%3Cpath_d='M1_1l5_5_5-5'_stroke='%23475569'_stroke-width='1.5'_fill='none'_stroke-linecap='round'/%3E%3C/svg%3E")] bg-no-repeat bg-[position:right_14px_center] pr-10`}
              value={formData.employment_status} 
              onChange={set('employment_status')}
            >
              {finalEmployment.map(v => (
                <option key={v} value={v} className="bg-slate-900 border-none">{v}</option>
              ))}
            </select>
          </FormGroup>
        </div>

        {/* Intro message */}
        <FormGroup label="Message to Host" icon={<MessageSquare size={13} className="text-blue-400" />}>
          <textarea
            className={`${inputBase} min-h-[130px] p-4 resize-y leading-relaxed font-medium`}
            placeholder="Introduce yourself and tell them why you'd be a great roommate…"
            value={formData.intro_message}
            onChange={set('intro_message')}
            required
          />
          <p className="text-[11px] text-slate-500 font-medium mt-1.5 flex justify-between">
            <span>Be honest and friendly for better compatibility.</span>
            <span>{formData.intro_message.length}/500</span>
          </p>
        </FormGroup>

        {/* Privacy note */}
        <div className="bg-blue-500/5 border border-blue-500/20 rounded-[14px] p-3.5 flex gap-3 items-start mb-5">
          <div className="w-8 h-8 bg-blue-500/10 border border-blue-500/20 rounded-lg flex items-center justify-center shrink-0">
            <ShieldCheck size={15} className="text-blue-400" />
          </div>
          <p className="text-xs text-blue-300 font-semibold leading-relaxed m-0">
            Your profile details will be shared with the room host for review.
            Make sure your profile is complete and up to date.
          </p>
        </div>

        {/* Submit */}
        <button 
          className="w-full h-14 bg-gradient-to-br from-blue-600 via-blue-500 to-indigo-600 text-white border-none rounded-2xl text-sm font-extrabold tracking-wider flex items-center justify-center gap-2 shadow-lg shadow-blue-500/20 cursor-pointer transition-all duration-200 hover:opacity-95 hover:shadow-xl hover:shadow-blue-500/30 active:scale-[0.98] disabled:opacity-60 disabled:cursor-not-allowed disabled:transform-none"
          type="submit" 
          disabled={submitting}
        >
          {submitting ? (
            <>
              <div className="w-[18px] h-[18px] border-2 border-white/30 border-t-white rounded-full animate-spin" />
              Submitting Application…
            </>
          ) : (
            <>
              <Send size={16} />
              Send Application
              <ChevronRight size={16} className="ml-0.5" />
            </>
          )}
        </button>
      </form>
    </div>
  );
}

/* ─── RIGHT PANEL ─── */
function RightPanel({ roomImg, roomTitle, roomRent, roomType, location, mates, ownerName, ownerPhoto, ownerQuote }) {
  return (
    <div className="flex flex-col gap-4">
      {/* Room Card */}
      <div className="animate-fade-up delay-200 bg-slate-900 rounded-[24px] border border-slate-800 shadow-xl overflow-hidden hover:border-blue-500/30 transition-all duration-300">
        {/* Image */}
        <div className="relative h-[220px]">
          <img
            src={roomImg}
            alt={roomTitle}
            className="w-full h-full object-cover block"
          />
          {/* Gradient overlay */}
          <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-slate-950/20 to-transparent" />
          
          {/* Price badge */}
          <div className="absolute bottom-3.5 left-3.5 bg-blue-600 rounded-xl px-4 py-2 shadow-lg shadow-blue-500/20 backdrop-blur-sm border border-blue-400/20">
            <span className="text-[17px] font-black text-white">{roomRent}</span>
            <span className="text-[11px] text-white/70 ml-1 font-bold">/mo</span>
          </div>
          
          {/* AI Pick badge */}
          <div className="absolute top-3 right-3 bg-emerald-500/90 backdrop-blur-md rounded-lg px-2.5 py-1.5 flex items-center gap-1.5 shadow-lg shadow-emerald-500/10 active:scale-95 transition-transform">
            <Sparkles size={11} className="text-white animate-pulse" />
            <span className="text-[10px] font-black text-white tracking-wider">AI PICK</span>
          </div>
        </div>

        <div className="px-5 pt-4 pb-5 bg-slate-900">
          <p className="text-lg font-black text-white mb-3 tracking-tight">
            {roomTitle}
          </p>

          {/* Info pills */}
          <div className="grid grid-cols-2 gap-2 mb-4">
            {[
              { icon: <Home size={13} className="text-blue-400" />, wrapper: 'bg-blue-500/10 border-blue-500/20', text: roomType },
              { icon: <Users size={13} className="text-indigo-400" />, wrapper: 'bg-indigo-500/10 border-indigo-500/20', text: `${mates} Roommates` },
              { icon: <MapPin size={13} className="text-amber-400" />, wrapper: 'bg-amber-500/10 border-amber-500/20', text: location },
              { icon: <Star size={13} className="text-emerald-400 fill-emerald-400" />, wrapper: 'bg-emerald-500/10 border-emerald-500/20', text: 'Top Rated' },
            ].map((p, i) => (
              <div key={i} className={`flex items-center gap-2 rounded-xl px-2.5 py-2.5 border ${p.wrapper}`}>
                <div className="w-5 h-5 flex items-center justify-center shrink-0">
                  {p.icon}
                </div>
                <span className="text-[11px] font-bold text-slate-300 whitespace-nowrap overflow-hidden text-ellipsis">
                  {p.text}
                </span>
              </div>
            ))}
          </div>

          {/* Host trust strip */}
          <div className="flex items-center gap-2 bg-slate-800/50 border border-slate-800 rounded-xl px-3 py-3">
            <BadgeCheck size={16} className="text-emerald-400 shrink-0" />
            <span className="text-[11px] font-bold text-slate-300 tracking-wide">
              Verified listing · Background checked host
            </span>
          </div>
        </div>
      </div>

      {/* Host Quote Card */}
      {ownerQuote && (
        <div className="animate-fade-up delay-300 bg-slate-900/50 backdrop-blur-xl border border-slate-800 rounded-[20px] p-5 relative shadow-xl ">
          <div className="absolute top-2 left-4 text-4xl text-blue-500/20 font-serif leading-none italic">“</div>
          <p className="text-slate-300 text-[13px] font-bold leading-relaxed italic relative z-10 pl-2 pr-2 mb-4 tracking-wide">
            {ownerQuote}
          </p>
          
          <div className="flex items-center gap-3 pt-3 border-t border-slate-800/50">
            <div className="w-10 h-10 rounded-xl overflow-hidden bg-slate-800 border-2 border-slate-700 shadow-lg">
              <img 
                src={ownerPhoto || `https://ui-avatars.com/api/?name=${ownerName || 'Host'}&background=1E63FF&color=fff`} 
                alt="Host" 
                className="w-full h-full object-cover"
              />
            </div>
            <div>
              <p className="text-[12px] font-black text-white m-0 tracking-tight">{ownerName || 'Verified Host'}</p>
              <div className="flex items-center gap-1 mt-0.5">
                {[1,2,3,4,5].map(s => <Star key={s} size={8} className="text-amber-400 fill-amber-400" />)}
                <span className="text-[9px] font-black text-slate-500 ml-1 uppercase tracking-tighter">Verified Host</span>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

/* ─── FORM GROUP ─── */
function FormGroup({ label, icon, children, noMb = false }) {
  return (
    <div className={noMb ? 'mb-0' : 'mb-5'}>
      <label className="flex items-center gap-1.5 text-[10px] font-black text-slate-400 uppercase tracking-[0.1em] mb-2.5 pl-0.5">
        <div className="w-5 h-5 bg-slate-800 rounded-md flex items-center justify-center">
          {icon}
        </div>
        {label}
      </label>
      {children}
    </div>
  );
}

/* ─── LOADING STATE ─── */
function LoadingState() {
  return (
    <div className="min-h-screen bg-slate-950">
      <div className="bg-slate-900/80 backdrop-blur-md h-16 border-b border-slate-800" />
      <div className="h-[240px] bg-slate-900 animate-pulse" />
      <div className="max-w-[1152px] mx-auto px-4 py-8 grid grid-cols-1 md:grid-cols-[1fr_420px] gap-6">
        <div className="bg-slate-900 animate-pulse h-[600px] rounded-3xl border border-slate-800" />
        <div className="flex flex-col gap-5">
          <div className="bg-slate-900 animate-pulse h-[340px] rounded-3xl border border-slate-800" />
          <div className="bg-slate-900 animate-pulse h-[240px] rounded-3xl border border-slate-800" />
        </div>
      </div>
    </div>
  );
}
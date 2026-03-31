import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { roomAPI, favoriteAPI } from '@/lib/api';
import {
  ArrowLeft, MapPin, Home, DollarSign, Users,
  Bath, DoorOpen, Star, Heart, MessageCircle,
  Calendar, ChevronRight, Share2, Info,
  ShieldCheck, CheckCircle2, UserCheck, Sparkles
} from 'lucide-react';
import { format } from 'date-fns';
import toast from 'react-hot-toast';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import { cn } from "@/lib/utils";

// ── Palette ───────────────────────────────────────────────────────────────────
const P = { blue: '#1E63FF', blueDark: '#0d1e3c' };

// ── Shared components ────────────────────────────────────────────────────────
function SectionHeader({ icon: Icon, iconBg, iconColor, title, subtitle, action }) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-3">
      <div className="flex items-center gap-3">
        <div className={cn("w-9 h-9 rounded-xl flex items-center justify-center shrink-0", iconBg)}>
          <Icon className={cn("w-4 h-4", iconColor)} />
        </div>
        <div>
          <h2 className="text-lg sm:text-xl font-extrabold text-foreground tracking-tight leading-tight">
            {title}
          </h2>
          <p className="text-[10px] text-muted-foreground font-semibold uppercase tracking-wider">
            {subtitle}
          </p>
        </div>
      </div>
      {action}
    </div>
  );
}

export default function RoomDetailPage() {
  const { roomId } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';
  const [room, setRoom] = useState(null);
  const [loading, setLoading] = useState(true);

  const avatarUrl = (name) => `https://ui-avatars.com/api/?name=${encodeURIComponent(name || 'U')}&background=1E63FF&color=fff&size=100`;

  useEffect(() => {
    if (!email || !roomId) return;
    roomAPI.getHomeRoomDetail(roomId, email).then(res => {
      if (res.data?.data) setRoom(res.data.data);
    }).catch(() => {}).finally(() => setLoading(false));
  }, [email, roomId]);

  if (loading) return (
    <div className="flex flex-col items-center justify-center py-32 gap-3">
      <div className="w-10 h-10 border-2 border-primary/20 border-t-primary rounded-full animate-spin" />
      <p className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest animate-pulse">Scanning Property Structure…</p>
    </div>
  );

  if (!room) return (
     <div className="flex flex-col items-center justify-center py-32 space-y-6 max-w-xl mx-auto text-center px-4">
       <div className="w-20 h-20 bg-muted/30 rounded-2xl flex items-center justify-center">
          <Home className="w-10 h-10 text-muted-foreground/30" />
       </div>
       <div className="space-y-2">
         <h3 className="text-2xl font-extrabold text-foreground tracking-tight">Listing unavailable</h3>
         <p className="text-sm text-muted-foreground font-medium italic">The room you are looking for might have been booked or removed by the landlord.</p>
       </div>
       <Button variant="outline" onClick={() => navigate(-1)} className="rounded-xl font-extrabold h-11 px-8 border-border/60">
          <ArrowLeft className="w-4 h-4 mr-2" /> Return to Discovery
       </Button>
     </div>
  );
  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Top Nav & Header ─────────────────────────────────────────────── */}
      <div className="flex items-center justify-between pt-4 sm:pt-6">
         <Button variant="ghost" size="sm" onClick={() => navigate(-1)} className="rounded-xl h-9 px-3 text-muted-foreground hover:text-primary gap-1.5 font-extrabold">
            <ArrowLeft className="w-4 h-4" /> Back
         </Button>
         <div className="flex gap-2">
            <Button variant="outline" size="icon" className="h-9 w-9 rounded-xl border-border/40 hover:bg-muted font-bold">
               <Share2 className="w-4 h-4" />
            </Button>
         </div>
      </div>

      {/* ── Image Showcase ────────────────────────────────────────────────── */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 sm:h-[480px]">
        <div className="md:col-span-2 relative rounded-2xl overflow-hidden border border-border/50 group">
          {room.photos?.[0]?.image ? (
            <img src={room.photos[0].image} alt="" className="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-105" />
          ) : (
            <div className="w-full h-full bg-muted flex items-center justify-center">
               <Home className="w-16 h-16 text-muted-foreground/20" />
            </div>
          )}
          <div className="absolute top-4 left-4">
             <Badge className="bg-white/90 backdrop-blur-md text-emerald-600 border-none font-extrabold text-[10px] px-3 py-1 rounded-lg shadow-lg flex items-center gap-1.5 uppercase tracking-widest">
                <CheckCircle2 size={12} /> Verified Property
             </Badge>
          </div>
        </div>
        <div className="md:col-span-2 grid grid-cols-2 gap-4">
          {[1, 2, 3, 4].map((idx) => (
            <div key={idx} className="relative rounded-2xl overflow-hidden border border-border/50 bg-muted group h-full sm:h-auto min-h-[140px]">
               {room.photos?.[idx]?.image ? (
                 <img src={room.photos[idx].image} alt="" className="w-full h-full object-cover transition-transform group-hover:scale-110" />
               ) : (
                 <div className="w-full h-full flex items-center justify-center">
                    <Sparkles className="w-8 h-8 text-muted-foreground/10" />
                 </div>
               )}
               {idx === 4 && room.photos?.length > 5 && (
                 <div className="absolute inset-0 bg-black/40 backdrop-blur-[2px] flex items-center justify-center">
                    <p className="text-white font-black text-sm uppercase tracking-widest">+{room.photos.length - 5} More</p>
                 </div>
               )}
            </div>
          ))}
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 sm:gap-12">
        <div className="lg:col-span-2 space-y-10 sm:space-y-12">
          {/* Detailed Specifications */}
          <section className="space-y-6">
             <div>
                <div className="flex items-center gap-2 mb-3">
                   <Badge className="bg-primary/8 text-primary border-none font-extrabold text-[9px] tracking-widest px-2.5 py-0.5 rounded-md uppercase">
                      {room.room_type || 'Premium Shared Room'}
                   </Badge>
                   <div className="flex items-center gap-1.5 text-[10px] text-muted-foreground font-extrabold uppercase tracking-widest bg-muted/40 px-2 py-0.5 rounded-md">
                      <Star className="w-3 h-3 text-amber-500 fill-amber-500" /> {room.rating || 4.7} Harmony Score
                   </div>
                </div>
                <h1 className="text-3xl sm:text-4xl font-extrabold text-foreground tracking-tight leading-tight mb-2">
                   {room.apartment_title}
                </h1>
                <p className="text-sm sm:text-base text-muted-foreground font-semibold flex items-center gap-2">
                   <MapPin className="w-4 h-4 text-primary/60" /> {room.address || room.city}
                </p>
             </div>

             <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
               {[
                 { icon: DollarSign, label: 'Monthly Repay', value: `₹${parseInt(room.monthly_rent).toLocaleString()}`, color: 'bg-emerald-500/10 text-emerald-600' },
                 { icon: Users, label: 'Capacity', value: `${room.roommate_count} Total`, color: 'bg-indigo-500/10 text-indigo-500' },
                 { icon: Bath, label: 'Amenities', value: room.bathroom_type?.replace('_', ' ') || 'Shared', color: 'bg-blue-500/10 text-blue-500' },
                 { icon: DoorOpen, label: 'Privileges', value: room.entry_type?.replace('_', ' ') || 'Regular', color: 'bg-amber-500/10 text-amber-600' },
               ].map((spec, i) => (
                 <div key={i} className="bg-card p-5 border border-border/50 shadow-sm flex flex-col items-center justify-center text-center rounded-2xl group hover:border-primary/30 hover:shadow-md transition-all">
                    <div className={cn("w-10 h-10 rounded-xl flex items-center justify-center mb-3 transition-transform group-hover:scale-110", spec.color)}>
                       <spec.icon className="w-5 h-5" />
                    </div>
                    <p className="text-sm font-extrabold text-foreground leading-tight">{spec.value}</p>
                    <p className="text-[9px] font-extrabold text-muted-foreground uppercase tracking-widest mt-1.5 opacity-60">{spec.label}</p>
                 </div>
               ))}
             </div>
          </section>

          {/* Description */}
          {room.description && (
            <section className="space-y-4">
              <SectionHeader
                icon={Info}
                iconBg="bg-primary/8"
                iconColor="text-primary"
                title="Property Narrative"
                subtitle="A personal take on this living space"
              />
              <div className="bg-card rounded-2xl p-6 sm:p-8 border border-border/50 shadow-sm relative overflow-hidden">
                <div className="absolute top-0 right-0 w-32 h-32 bg-primary/5 rounded-full blur-3xl -mr-16 -mt-16" />
                <p className="text-sm sm:text-base text-muted-foreground font-semibold leading-relaxed relative z-10 italic">
                  "{room.description}"
                </p>
              </div>
            </section>
          )}

          {/* Amenities */}
          {room.tags && (
             <section className="space-y-4">
               <SectionHeader
                 icon={Star}
                 iconBg="bg-amber-500/10"
                 iconColor="text-amber-500"
                 title="Key Features"
                 subtitle="Standard and premium inclusions"
               />
               <div className="flex flex-wrap gap-2.5">
                  {room.tags.split(',').filter(t => t).map((tag, i) => (
                    <div key={i} className="px-4 py-2 border border-border/50 rounded-xl bg-card text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest hover:border-primary/20 hover:text-primary transition-all cursor-default">
                       {tag.trim()}
                    </div>
                  ))}
               </div>
             </section>
          )}

          {/* Potential Roommates */}
          {room.potential_roommates?.length > 0 && (
            <section className="space-y-4">
               <SectionHeader
                 icon={Users}
                 iconBg="bg-blue-500/10"
                 iconColor="text-blue-500"
                 title="Compatibility Radar"
                 subtitle="People our AI group suggested for this room"
               />
               <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {room.potential_roommates.map((rm, i) => (
                  <div
                    key={i}
                    onClick={() => navigate(`/dashboard/roommate/${rm.email}`)}
                    className="flex items-center gap-4 p-4 rounded-2xl bg-card border border-border/50 hover:border-primary/25 hover:shadow-md transition-all cursor-pointer group"
                  >
                    <div className="relative shrink-0">
                       <img src={rm.photo || avatarUrl(rm.full_name)} alt="" className="w-12 h-12 rounded-xl object-cover border-2 border-card shadow-sm group-hover:scale-105 transition-transform" />
                       <div className="absolute -bottom-1 -right-1 w-5 h-5 bg-emerald-500 rounded-lg border-2 border-card flex items-center justify-center text-white">
                          <CheckCircle2 size={10} />
                       </div>
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-extrabold text-foreground truncate group-hover:text-primary transition-colors">{rm.full_name}</p>
                      <Badge className="bg-emerald-50 text-emerald-600 border-none font-extrabold text-[8px] px-1.5 py-0 rounded-md mt-1">
                          {Math.round(rm.match_percentage)}% MATCH
                      </Badge>
                    </div>
                    <ChevronRight size={14} className="text-muted-foreground group-hover:translate-x-1 transition-transform" />
                  </div>
                ))}
              </div>
            </section>
          )}
        </div>

        {/* ── Sidebar Stats ────────────────────────────────────────────────── */}
        <div className="space-y-6 sm:space-y-8 lg:sticky lg:top-24 h-fit pb-10">
          {/* Landlord Identity */}
          <div className="bg-card rounded-2xl p-6 sm:p-8 border border-border/50 shadow-md relative overflow-hidden group">
            <div className="absolute top-0 left-0 right-0 h-1 bg-primary/20" />
            <div className="flex items-center gap-3 mb-6">
               <div className="w-8 h-8 rounded-lg bg-primary/8 flex items-center justify-center">
                  <UserCheck size={16} className="text-primary" />
               </div>
               <span className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest">Verified Host</span>
            </div>

            <div className="flex items-center gap-4 mb-8">
              <img src={room.owner_photo || avatarUrl(room.owner_name)} alt="" className="w-16 h-16 rounded-xl object-cover border-2 border-card shadow-md group-hover:scale-105 transition-transform duration-500" />
              <div className="flex-1 min-w-0">
                <p className="font-extrabold text-foreground text-base truncate mb-0.5">{room.owner_name}</p>
                <div className="flex items-center gap-0.5">
                   {[1,2,3,4,5].map(i => <Star key={i} size={10} className="text-amber-500 fill-amber-500" />)}
                </div>
              </div>
            </div>

            <div className="space-y-2">
               <Button onClick={() => navigate(`/dashboard/roommate/${room.owner_email}`)} variant="outline" className="w-full h-11 rounded-xl font-extrabold text-xs border-border/60 hover:bg-primary/5 hover:text-primary transition-all">
                  View Host Protocol
               </Button>
               <Button variant="ghost" className="w-full h-11 rounded-xl text-xs font-extrabold text-muted-foreground hover:bg-muted/50 hover:text-foreground">
                  Quick Message
               </Button>
            </div>
          </div>

          {/* Checkout Component */}
          <div className="rounded-2xl p-6 sm:p-8 text-white shadow-2xl shadow-blue-900/10 relative overflow-hidden"
               style={{ background: `linear-gradient(135deg, ${P.blueDark} 0%, #0d1e3c 100%)` }}>
            <div className="absolute top-0 right-0 w-32 h-32 bg-white/5 rounded-full blur-3xl -mr-16 -mt-16" />

            <div className="mb-8 relative z-10">
               <p className="text-[9px] font-extrabold tracking-widest text-blue-200/60 uppercase mb-2">Investment Structure</p>
               <div className="flex items-baseline gap-1">
                  <span className="text-4xl sm:text-5xl font-extrabold tracking-tighter">₹{parseInt(room.monthly_rent).toLocaleString()}</span>
                  <span className="text-sm text-blue-200/40 font-bold uppercase tracking-widest">/mo</span>
               </div>
               <div className="flex items-center gap-1.5 mt-4 text-[10px] font-extrabold text-emerald-400 uppercase tracking-widest">
                  <ShieldCheck size={12} /> Deposit Protection Active
               </div>
            </div>

            <div className="space-y-4 relative z-10">
              {room.status === 'AVAILABLE' ? (
                <Button onClick={() => navigate(`/dashboard/room-share/form/${roomId}`)} size="lg" className="w-full h-14 rounded-xl bg-white text-primary hover:bg-blue-50 font-extrabold text-sm shadow-xl shadow-black/20 hover:-translate-y-1 transition-all">
                  Initiate Booking
                </Button>
              ) : (
                <Button disabled size="lg" className="w-full h-14 rounded-xl bg-white/20 text-white/60 font-extrabold text-sm border border-white/10 cursor-not-allowed">
                  Booked
                </Button>
              )}
              <Button variant="ghost" size="lg" className="w-full h-14 rounded-xl border border-white/10 hover:bg-white/5 font-extrabold text-white text-xs gap-2 uppercase tracking-widest">
                <Calendar size={14} /> Schedule View
              </Button>
            </div>

            <div className="mt-8 pt-6 border-t border-white/5 space-y-2 relative z-10">
               <div className="flex items-center justify-between text-[9px] font-extrabold uppercase tracking-widest text-blue-200/40">
                  <span>Activation:</span>
                  <span className="text-blue-100">{room.available_from ? format(new Date(room.available_from), "MMM dd, yyyy") : 'Immediate'}</span>
               </div>
               <div className="flex items-center justify-between text-[9px] font-extrabold uppercase tracking-widest text-blue-200/40">
                  <span>Commitment:</span>
                  <span className="text-blue-100">Monthly Rolling</span>
               </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

import { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { matchAPI, hotelAPI, favoriteAPI, roomAPI } from '@/lib/api';
import { useNavigate } from 'react-router-dom';
import {
  Heart, MapPin, Star, Trash2, ArrowRight,
  Home, Users, Briefcase, Zap, ShieldCheck
} from 'lucide-react';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import toast from 'react-hot-toast';

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

export default function SavedPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';

  const [roommates, setRoommates] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!email) return;
    fetchData();
  }, [email]);

  const fetchData = async () => {
    setLoading(true);
    try {
      const rmRes = await favoriteAPI.getAll(email);
      setRoommates(rmRes.data?.favorites || []);
    } catch {
      toast.error('Failed to retrieve your collection');
    } finally {
      setLoading(false);
    }
  };

  const removeRoommate = async (targetEmail) => {
    try {
      await favoriteAPI.save(email, targetEmail); // Toggles
      setRoommates(prev => prev.filter(r => r.email !== targetEmail));
      toast.success('Removed from radar');
    } catch { toast.error('Action failed'); }
  };

  const tabs = [
    { id: 'roommates', label: 'Match Matrix', icon: Users, count: roommates.length },
  ];

  if (loading) return (
     <div className="flex flex-col items-center justify-center py-24 gap-3">
        <div className="w-10 h-10 border-2 border-primary/20 border-t-primary rounded-full animate-spin" />
        <p className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest animate-pulse">Synchronizing Collection…</p>
     </div>
  );

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Header ── */}
      <div className="mt-4 sm:mt-6">
        <SectionHeader
          icon={Heart}
          iconBg="bg-rose-500/10"
          iconColor="text-rose-500"
          title="Digital Collection"
          subtitle="Curated shortcuts to your most compatible matches"
        />
      </div>

      {/* ── Grid Rendering ── */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {roommates.length > 0 ? roommates.map(rm => (
          <Card key={rm.email} className="group overflow-hidden rounded-2xl border-border/50 hover:border-primary/30 transition-all hover:shadow-xl hover:shadow-primary/5">
            <div className="p-5 flex items-center gap-4">
              <img src={rm.photo || `https://ui-avatars.com/api/?name=${encodeURIComponent(rm.full_name || 'U')}&background=1E63FF&color=fff`} className="w-16 h-16 rounded-xl object-cover border-2 border-card shadow-sm" alt="" />
              <div className="flex-1 min-w-0">
                <h3 className="font-extrabold text-foreground truncate">{rm.full_name}</h3>
                <p className="text-[10px] text-muted-foreground font-semibold flex items-center gap-1 mt-1">
                  <MapPin size={10} /> {rm.city || 'Chennai'}
                </p>
                <Badge className="bg-primary/10 text-primary border-none text-[8px] font-black mt-2">
                  {rm.match_percentage}% COMPATIBILITY
                </Badge>
              </div>
            </div>
            <div className="px-5 pb-5 flex gap-2">
              <Button onClick={() => navigate(`/dashboard/roommate/${rm.email}`)} className="flex-1 h-9 rounded-lg font-extrabold text-[10px] uppercase tracking-widest gap-2">
                 Open Profile <ArrowRight size={12} />
              </Button>
              <Button variant="ghost" size="icon" onClick={() => removeRoommate(rm.email)} className="h-9 w-9 rounded-lg text-rose-500 hover:bg-rose-50">
                <Trash2 size={16} />
              </Button>
            </div>
          </Card>
        )) : <EmptyState icon={Users} msg="No teammates saved yet." />}
      </div>
    </div>
  );
}

function EmptyState({ icon: Icon, msg }) {
  return (
    <div className="col-span-full py-20 flex flex-col items-center justify-center text-center space-y-4">
      <div className="w-16 h-16 bg-muted/30 rounded-2xl flex items-center justify-center">
         <Icon className="w-8 h-8 text-muted-foreground/30" />
      </div>
      <p className="text-muted-foreground text-sm font-semibold italic">{msg}</p>
    </div>
  );
}

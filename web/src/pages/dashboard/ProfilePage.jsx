import { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { profileAPI } from '@/lib/api';
import { useNavigate } from 'react-router-dom';
import {
  User, Mail, MapPin, Briefcase, Calendar,
  DollarSign, Shield, Edit3, Camera, Clock,
  Home, ChevronRight, LogOut, Check, Save, X, Sparkles, Users, MessageSquare, Settings, Star,
} from 'lucide-react';
import toast from 'react-hot-toast';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";
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

const InfoRow = ({ icon: Icon, label, value, field, type = "text", options = null, editing, form, setForm }) => (
  <div className="flex items-start gap-4 py-4 px-1 group">
    <div className="w-10 h-10 rounded-xl bg-primary/8 flex items-center justify-center shrink-0 group-hover:bg-primary transition-colors group-hover:shadow-lg group-hover:shadow-primary/20">
      <Icon className="w-4.5 h-4.5 text-primary group-hover:text-white transition-colors" />
    </div>
    <div className="flex-1 min-w-0">
      <p className="text-[10px] text-muted-foreground font-extrabold uppercase tracking-widest mb-1.5">{label}</p>
      {editing && field ? (
        options ? (
          <Select
            value={form[field] || ''}
            onValueChange={(val) => setForm(p => ({ ...p, [field]: val }))}
          >
            <SelectTrigger className="w-full h-9 bg-muted/30 border-border/40 hover:border-primary/20 transition-all font-extrabold text-xs rounded-xl">
              <SelectValue placeholder={`Select ${label.toLowerCase()}`} />
            </SelectTrigger>
            <SelectContent className="rounded-xl border-border/40">
              {options.map(opt => (
                <SelectItem key={opt} value={opt} className="font-extrabold text-xs">{opt}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        ) : (
          <Input
            type={type}
            value={form[field] || ''}
            onChange={(e) => setForm(prev => ({ ...prev, [field]: e.target.value }))}
            className="h-9 bg-muted/30 border-border/40 focus:bg-background focus:ring-primary/10 transition-all font-extrabold text-xs rounded-xl text-foreground placeholder:text-muted-foreground/40"
          />
        )
      ) : (
        <p className="font-extrabold text-foreground text-[13px] sm:text-sm truncate leading-tight">
          {value || 'Not set'}
        </p>
      )}
    </div>
  </div>
);

export default function ProfilePage() {
  const { user, isPremium, logout, refreshUser } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [form, setForm] = useState({});

  useEffect(() => {
    if (!email) return;
    profileAPI.getDashboard(email).then(res => {
      if (res.data?.success) {
        setProfile(res.data.data?.profile);
        setForm(res.data.data?.profile || {});
      }
    }).catch(() => {}).finally(() => setLoading(false));
  }, [email]);

  const handleSave = async () => {
    try {
      const fd = new FormData();
      fd.append('email', email);

      const fields = {
        full_name: form.full_name || form.fullName || '',
        room_status: form.room_status || form.roomStatus || 'SEEKING_ROOM',
        age: form.age || '',
        about_me: form.about_me || form.aboutMe || '',
        occupation: form.occupation || '',
        target_area: form.target_area || form.targetArea || '',
        budget_range: form.budget_range || form.budgetRange || '',
        move_in_date: form.move_in_date || form.moveInDate || '',
        address: form.address || '',
      };

      Object.entries(fields).forEach(([key, val]) => {
        fd.append(key, val);
      });

      const res = await profileAPI.update(fd);
      if (res.data?.success) {
        toast.success('Profile updated successfully');
        const updatedProfile = res.data.data;
        setProfile(updatedProfile);
        setForm(updatedProfile);
        setEditing(false);
        refreshUser();
      } else {
        toast.error('Failed to update profile');
      }
    } catch { toast.error('Something went wrong. Please try again.'); }
  };

  const getCompleteness = () => {
    if (!form) return 0;
    const essentialFields = [
      ['full_name', 'fullName'],
      ['age'],
      ['occupation'],
      ['about_me', 'aboutMe'],
      ['target_area', 'targetArea'],
      ['budget_range', 'budgetRange'],
      ['move_in_date', 'moveInDate'],
      ['address']
    ];

    let filled = 0;
    essentialFields.forEach(aliasSet => {
      const isFilled = aliasSet.some(field => {
        const val = form[field];
        return val !== undefined && val !== null && String(val).trim() !== '';
      });
      if (isFilled) filled++;
    });

    return Math.min(100, Math.round((filled / essentialFields.length) * 100));
  };

  const compPercent = getCompleteness();
  const avatarUrl = (name) => `https://ui-avatars.com/api/?name=${encodeURIComponent(name || 'U')}&background=1E63FF&color=fff&size=200`;

  if (loading) return (
    <div className="flex flex-col items-center justify-center py-20 pointer-events-none gap-3">
      <div className="w-10 h-10 border-2 border-primary/20 border-t-primary rounded-full animate-spin" />
      <p className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest animate-pulse">Syncing Identity…</p>
    </div>
  );

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Profile Header ───────────────────────────────────────────────── */}
      <div className="bg-card rounded-2xl border border-border/50 shadow-md overflow-hidden group">
        <div
          className="h-32 sm:h-44 relative"
          style={{ background: `linear-gradient(135deg, ${P.blue} 0%, ${P.blueDark} 100%)` }}
        >
          {/* Decorative grid */}
          <div className="absolute inset-0 opacity-[0.05]" style={{ backgroundImage: "repeating-linear-gradient(0deg,transparent,transparent 28px,#fff 28px,#fff 29px),repeating-linear-gradient(90deg,transparent,transparent 28px,#fff 28px,#fff 29px)" }} />
          <div className="absolute -top-10 -right-10 w-48 h-48 bg-white/10 rounded-full blur-[80px]" />

          {editing && (
            <div className="absolute top-4 right-4 flex gap-2">
              <Button onClick={() => setEditing(false)} variant="secondary" size="sm" className="bg-white/10 backdrop-blur-md text-white border-white/20 hover:bg-white/20 rounded-xl font-extrabold text-xs h-9">
                <X className="w-3.5 h-3.5 mr-1.5" /> Cancel
              </Button>
              <Button onClick={handleSave} size="sm" className="bg-white text-primary hover:bg-white/90 rounded-xl shadow-xl font-extrabold text-xs h-9">
                <Check className="w-3.5 h-3.5 mr-1.5" /> Save
              </Button>
            </div>
          )}
        </div>

        <div className="px-6 sm:px-10 pb-8 sm:pb-10">
          <div className="relative -mt-16 sm:-mt-20 flex flex-col sm:flex-row items-center sm:items-end gap-6 mb-8">
            <div className="relative group/avatar">
              <img
                src={profile?.photo || avatarUrl(profile?.full_name || profile?.fullName)}
                alt=""
                className="w-32 h-32 sm:w-40 sm:h-40 rounded-2xl object-cover border-[6px] border-card shadow-lg transition-transform duration-500 group-hover/avatar:scale-[1.02]"
              />
              <div className="absolute -bottom-1 -right-1 w-11 h-11 rounded-xl bg-primary flex items-center justify-center cursor-pointer border-4 border-card shadow-lg hover:scale-110 transition-all">
                <Camera className="w-5 h-5 text-white" />
              </div>
            </div>

            <div className="flex-1 text-center sm:text-left mb-2">
              <div className="flex flex-col sm:flex-row items-center gap-3">
                <h1 className="text-2xl sm:text-3xl font-extrabold text-foreground tracking-tight leading-none group-hover:text-primary transition-colors">
                  {profile?.fullName || profile?.full_name || 'Anonymous User'}
                </h1>
                {isPremium && (
                  <Badge className="bg-primary/10 text-primary border-primary/20 font-extrabold text-[10px] px-3 py-0.5 rounded-lg flex items-center gap-1 shadow-sm shadow-primary/10">
                    <Sparkles className="w-3 h-3 text-primary shrink-0" /> Premium User
                  </Badge>
                )}
                {compPercent === 100 && (
                  <Badge className="bg-emerald-50 text-emerald-600 border-none font-extrabold text-[10px] px-3 py-0.5 rounded-lg flex items-center gap-1">
                    <Check className="w-3 h-3" /> Verified
                  </Badge>
                )}
              </div>
              <p className="text-xs sm:text-sm text-muted-foreground font-extrabold flex items-center justify-center sm:justify-start gap-1.5 mt-2 tracking-wide">
                <Mail className="w-3.5 h-3.5 text-primary/60" /> {email}
              </p>
            </div>

            {!editing && (
              <Button
                onClick={() => setEditing(true)}
                className="rounded-xl h-10 px-8 font-extrabold text-xs shadow-lg shadow-primary/15 hover:shadow-xl hover:shadow-primary/25 transition-all hover:-translate-y-0.5"
              >
                <Edit3 className="w-3.5 h-3.5 mr-2" /> Edit Details
              </Button>
            )}
          </div>

          {/* Completeness bar */}
          <div className="bg-muted/30 rounded-2xl p-4 sm:p-5 border border-border/40">
            <div className="flex items-center justify-between mb-3">
              <p className="text-[10px] font-extrabold text-muted-foreground flex items-center gap-2 uppercase tracking-widest">
                <Shield className="w-3.5 h-3.5 text-primary" /> Profile Reliability Index
              </p>
              <div className="flex items-center gap-1.5">
                <span className="text-[11px] font-extrabold text-primary">{compPercent}% Complete</span>
              </div>
            </div>
            <div className="w-full h-2.5 bg-muted/60 rounded-full overflow-hidden p-[2px] border border-border/20">
              <div
                className={cn(
                  "h-full rounded-full transition-all duration-1000 ease-out shadow-sm",
                  compPercent < 40 ? "bg-rose-500" : compPercent < 80 ? "bg-amber-500" : "bg-primary"
                )}
                style={{ width: `${compPercent}%` }}
              />
            </div>
          </div>
        </div>
      </div>

      <div className="grid lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-8">
          {/* Main sections */}
          <div className="bg-card rounded-2xl p-6 sm:p-8 border border-border/50 shadow-md">
            <SectionHeader
              icon={User}
              iconBg="bg-primary/8"
              iconColor="text-primary"
              title="Personal Identity"
              subtitle="Basic details used for professional matching"
            />
            <div className="grid sm:grid-cols-2 gap-x-8 gap-y-2 mt-6">
              <InfoRow icon={User} label="Full Name" value={profile?.fullName || profile?.full_name} field="full_name" editing={editing} form={form} setForm={setForm} />
              <InfoRow icon={Calendar} label="Age" value={profile?.age} field="age" type="number" editing={editing} form={form} setForm={setForm} />
              <InfoRow icon={Briefcase} label="Occupation" value={profile?.occupation} field="occupation" editing={editing} form={form} setForm={setForm} />
              <InfoRow icon={MapPin} label="Current Location" value={profile?.address} field="address" editing={editing} form={form} setForm={setForm} />
            </div>

            <div className="mt-8 pt-8 border-t border-border/40">
              <SectionHeader
                icon={Sparkles}
                iconBg="bg-amber-500/10"
                iconColor="text-amber-500"
                title="Roommate Profile"
                subtitle="Preferences for AI discovery & matching"
              />
              <div className="grid sm:grid-cols-2 gap-x-8 gap-y-2 mt-6">
                <InfoRow icon={MapPin} label="Target Area" value={profile?.targetArea || profile?.target_area} field="target_area" editing={editing} form={form} setForm={setForm} />
                <InfoRow icon={DollarSign} label="Monthly Budget" value={profile?.budgetRange || profile?.budget_range} field="budget_range" editing={editing} form={form} setForm={setForm} />
                <InfoRow icon={Calendar} label="Desired Move-in" value={profile?.moveInDate || profile?.move_in_date} field="move_in_date" type="date" editing={editing} form={form} setForm={setForm} />
                <InfoRow
                  icon={Users}
                  label="Match Status"
                  value={profile?.roomStatus?.replace('_', ' ') || profile?.room_status?.replace('_', ' ')}
                  field="room_status"
                  options={["SEEKING_ROOM", "HAS_ROOM", "NOT_LOOKING"]}
                  editing={editing} form={form} setForm={setForm}
                />
              </div>
            </div>
          </div>

          {/* About Section */}
          <div className="bg-card rounded-2xl p-6 sm:p-8 border border-border/50 shadow-md">
            <SectionHeader
              icon={MessageSquare}
              iconBg="bg-indigo-500/10"
              iconColor="text-indigo-500"
              title="About Me"
              subtitle="Express your lifestyle and personality"
            />
            <div className="mt-6">
              {editing ? (
                <Textarea
                  value={form.about_me || form.aboutMe || ''}
                  onChange={(e) => setForm(prev => ({ ...prev, about_me: e.target.value }))}
                  rows={5}
                  className="w-full text-xs font-extrabold focus:ring-primary/10 rounded-xl p-4 border-border/40 bg-muted/20"
                  placeholder="Tell potential roommates about your lifestyle, hobbies, and what you're looking for..."
                />
              ) : (
                <p className="text-sm text-muted-foreground leading-relaxed font-extrabold opacity-90">
                  {profile?.aboutMe || profile?.about_me || 'No bio yet. Tell potential roommates about yourself to increase your match rate!'}
                </p>
              )}
            </div>
          </div>
        </div>

        {/* Sidebar / Menu */}
        <div className="space-y-6">
          <div
            className="rounded-2xl p-8 text-white shadow-xl shadow-blue-900/10 relative overflow-hidden group"
            style={{ background: `linear-gradient(135deg, ${P.blueDark} 0%, #0d1e3c 100%)` }}
          >
            <div className="absolute top-0 right-0 w-32 h-32 bg-white/5 rounded-full blur-3xl" />
            <h3 className="text-base font-extrabold mb-5 tracking-tight relative z-10">Account Management</h3>
            <div className="space-y-3 relative z-10">
              {[
                { icon: Clock, label: 'Booking History', to: '/dashboard/booking-history' },
                { icon: Settings, label: 'Account Settings', to: '/dashboard/settings' },
                { icon: Shield, label: 'Privacy & Data', to: '/dashboard/settings' },
              ].map((item, i) => (
                <button
                  key={i}
                  onClick={() => navigate(item.to)}
                  className="w-full flex items-center gap-3 p-3 rounded-xl hover:bg-white/10 transition-all font-extrabold group/btn"
                >
                  <div className="w-8 h-8 rounded-lg bg-white/10 flex items-center justify-center group-hover/btn:scale-110 group-hover/btn:bg-white group-hover/btn:text-primary transition-all">
                    <item.icon className="w-4 h-4" />
                  </div>
                  <span className="flex-1 text-left text-xs uppercase tracking-wider">{item.label}</span>
                  <ChevronRight size={12} className="opacity-40 group-hover/btn:opacity-100 group-hover/btn:translate-x-1 transition-all" />
                </button>
              ))}
            </div>
            <button
               onClick={() => { logout(); navigate('/login'); }}
               className="w-full mt-6 py-3 px-4 rounded-xl border border-white/10 text-rose-300 hover:bg-rose-500/10 hover:text-rose-100 transition-all font-extrabold text-[11px] uppercase tracking-widest flex items-center justify-center gap-2"
            >
               <LogOut size={14} /> Power Off / Log Out
            </button>
          </div>

          <div className="bg-card rounded-2xl p-6 sm:p-8 border border-border/50 shadow-md">
            <div className="flex items-center gap-3 mb-4">
              <div className="w-10 h-10 rounded-xl bg-amber-500/10 flex items-center justify-center">
                <Star className="w-5 h-5 text-amber-500" />
              </div>
              <h3 className="text-base font-extrabold text-foreground">Premium Status</h3>
            </div>
            {isPremium ? (
              <p className="text-xs text-muted-foreground font-extrabold leading-relaxed">
                You are a <strong>Premium RoomShare Member</strong>. Enjoy unlimited AI assistant queries and priority matching.
              </p>
            ) : (
              <>
                <p className="text-xs text-muted-foreground font-extrabold leading-relaxed">
                  Upgrade to unlock the full power of <strong>RoomShare AI</strong> and find matches 10x faster.
                </p>
                <Button
                  onClick={() => navigate('/dashboard/pricing')}
                  className="w-full mt-6 rounded-xl font-extrabold text-[10px] uppercase tracking-widest bg-amber-500 hover:bg-amber-600 text-white h-10 shadow-lg shadow-amber-500/20"
                >
                  Upgrade Now
                </Button>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

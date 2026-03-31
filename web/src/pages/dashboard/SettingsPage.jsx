import { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { profileAPI } from '@/lib/api';
import { useNavigate } from 'react-router-dom';
import {
  ArrowLeft, Bell, Globe, Lock, Mail, Key,
  Trash2, Shield, ChevronRight, AlertCircle,
  Eye, EyeOff, Settings
} from 'lucide-react';
import toast from 'react-hot-toast';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Switch } from "@/components/ui/switch";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
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

export default function SettingsPage() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || localStorage.getItem('user_email') || '';
  const [settings, setSettings] = useState({});
  const [loading, setLoading] = useState(true);

  const [showEmailDialog, setShowEmailDialog] = useState(false);
  const [showPasswordDialog, setShowPasswordDialog] = useState(false);
  const [showDeleteDialog, setShowDeleteDialog] = useState(false);

  const [newEmail, setNewEmail] = useState('');
  const [oldPwd, setOldPwd] = useState('');
  const [newPwd, setNewPwd] = useState('');
  const [delPwd, setDelPwd] = useState('');

  useEffect(() => {
    if (!email) return;
    profileAPI.getAccountSettings(email).then(res => {
      if (res.data?.data) setSettings(res.data.data);
    }).catch(() => {}).finally(() => setLoading(false));
  }, [email]);

  const handleToggleNotifs = async () => {
    const newVal = !settings.notifications_enabled;
    try {
      await profileAPI.updateAccountSettings({ email, notifications_enabled: newVal });
      setSettings(prev => ({ ...prev, notifications_enabled: newVal }));
      toast.success(newVal ? 'Notifications enabled' : 'Notifications disabled');
    } catch { toast.error('Update failed'); }
  };

  const handleChangeEmail = async () => {
    try {
      if (!newEmail) return toast.error('Please enter a new email');
      const res = await profileAPI.changeEmail({ current_email: email, new_email: newEmail });
      if (res.data?.success) {
        toast.success('Email changed! Please log in again.');
        logout();
        navigate('/login');
      } else toast.error(res.data?.error || 'Failed to change email');
    } catch { toast.error('Network error'); }
  };

  const handleChangePassword = async () => {
    try {
      if (!oldPwd || !newPwd) return toast.error('Please fill all fields');
      const res = await profileAPI.changePassword({ email, old_password: oldPwd, new_password: newPwd });
      if (res.data?.success) {
        toast.success('Password updated successfully!');
        setShowPasswordDialog(false);
        setOldPwd(''); setNewPwd('');
      } else toast.error(res.data?.error || 'Incorrect current password');
    } catch { toast.error('Update failed'); }
  };

  const handleDeleteAccount = async () => {
    try {
      if (!delPwd) return toast.error('Please enter your password');
      const res = await profileAPI.deleteAccount({ email, password: delPwd });
      if (res.data?.success) {
        toast.success('Account deleted forever.');
        logout();
        navigate('/');
      } else toast.error(res.data?.error || 'Incorrect password');
    } catch { toast.error('Deletion failed'); }
  };

  const SettingItem = ({ icon: Icon, label, desc, action, danger, children }) => (
    <div className={cn(
      "flex items-center gap-5 p-5 sm:p-6 transition-all duration-300 group hover:bg-muted/10 cursor-pointer",
      danger && "hover:bg-rose-500/[0.03]"
    )} onClick={action}>
      <div className={cn(
        "w-11 h-11 rounded-2xl flex items-center justify-center shrink-0 border border-border/10 shadow-sm transition-transform group-hover:scale-110",
        danger ? "bg-rose-500/10 text-rose-500" : "bg-primary/8 text-primary"
      )}>
        <Icon className="w-5 h-5" />
      </div>
      <div className="flex-1 min-w-0">
        <p className={cn(
          "font-extrabold text-[15px] tracking-tight mb-0.5",
          danger ? "text-rose-600" : "text-foreground group-hover:text-primary transition-colors"
        )}>{label}</p>
        <p className="text-xs text-muted-foreground font-semibold leading-normal opacity-80">{desc}</p>
      </div>
      <div className="shrink-0 flex items-center" onClick={(e) => e.stopPropagation()}>
        {children || (
          <div className="w-8 h-8 rounded-xl bg-muted/30 flex items-center justify-center text-muted-foreground group-hover:text-primary transition-all">
            <ChevronRight className="w-4 h-4" />
          </div>
        )}
      </div>
    </div>
  );

  if (loading) return (
     <div className="flex flex-col items-center justify-center py-24 gap-3">
        <div className="w-10 h-10 rounded-full border-2 border-primary/20 border-t-primary animate-spin" />
        <p className="text-[10px] font-extrabold text-muted-foreground uppercase tracking-widest animate-pulse">
           Opening Secure Settings…
        </p>
     </div>
  );

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* ── Header ───────────────────────────────────────────────────────── */}
      <div className="mt-4 sm:mt-6">
        <SectionHeader
          icon={Settings}
          iconBg="bg-primary/8"
          iconColor="text-primary"
          title="Account Intelligence"
          subtitle="Privacy, security AND system-wide preferences"
          action={
             <Button variant="ghost" size="sm" onClick={() => navigate(-1)} className="rounded-xl h-9 px-3 text-muted-foreground hover:text-primary gap-1.5 font-extrabold">
                <ArrowLeft className="w-4 h-4" /> Back
             </Button>
          }
        />
      </div>

      <div className="grid lg:grid-cols-1 gap-8 max-w-4xl mx-auto">
        <div className="bg-card rounded-2xl border border-border/50 shadow-md divide-y divide-border/30 overflow-hidden">
          <SettingItem
            icon={Bell}
            label="Pulse Notifications"
            desc="Control how we alert you about matches and secure messages."
          >
            <div className="scale-90 pr-1">
              <Switch
                checked={settings.notifications_enabled}
                onCheckedChange={handleToggleNotifs}
              />
            </div>
          </SettingItem>

          <SettingItem
            icon={Mail}
            label="Verified Identity / Email"
            desc={email}
            action={() => setShowEmailDialog(true)}
          />

          <SettingItem
            icon={Key}
            label="Access Credentials"
            desc="Frequent password rotations enhance your profile security score."
            action={() => setShowPasswordDialog(true)}
          />

          <SettingItem
            icon={Shield}
            label="Digital Footprint"
            desc="Review data transparency and privacy governance protocols."
            action={() => navigate('/dashboard/privacy')}
          />

          <SettingItem
            icon={Trash2}
            label="Termination Phase"
            desc="Irreversibly purge all matching data and identity files."
            action={() => setShowDeleteDialog(true)}
            danger
          />
        </div>

        {/* Global Safety Note */}
        <section
          className="rounded-3xl border border-primary/20 p-6 sm:p-10 flex flex-col md:flex-row items-center gap-8 relative overflow-hidden text-white"
          style={{ background: `linear-gradient(135deg, ${P.blueDark} 0%, #061121 100%)` }}
        >
          <div className="absolute top-0 left-0 w-64 h-64 bg-primary/10 rounded-full blur-[100px] -ml-32 -mt-32" />

          <div className="w-16 h-16 rounded-2xl bg-white/10 flex items-center justify-center shrink-0 border border-white/20 shadow-lg">
            <Lock className="w-8 h-8 text-white" />
          </div>

          <div className="flex-1 text-center md:text-left relative z-10">
            <h2 className="text-xl sm:text-2xl font-extrabold mb-2 tracking-tight">Security-First Infrastructure</h2>
            <p className="text-blue-100/60 text-xs sm:text-sm font-semibold max-w-xl leading-relaxed">
              RoomShare AI uses AES-256 encryption for all direct communications.
              We recommend using unique passwords and enabling multi-factor authentication where possible.
            </p>
          </div>

          <Button
            size="sm"
            className="bg-white text-primary hover:bg-white/90 rounded-xl font-extrabold h-10 px-6 shrink-0 shadow-lg text-xs relative z-10"
          >
            Review Audit Logs
          </Button>
        </section>
      </div>

      {/* Dialogs — Unified Style */}
      <Dialog open={showEmailDialog} onOpenChange={setShowEmailDialog}>
        <DialogContent className="rounded-2xl max-w-md p-0 border-none overflow-hidden sm:mt-0 mt-20">
          <div className="p-8 sm:p-10 space-y-6 bg-card">
            <div className="text-center space-y-2">
              <div className="w-12 h-12 rounded-2xl bg-primary/10 flex items-center justify-center mx-auto mb-4">
                 <Mail className="w-6 h-6 text-primary" />
              </div>
              <DialogTitle className="text-xl font-extrabold text-foreground tracking-tight">Migrate Digital Identity</DialogTitle>
              <DialogDescription className="text-xs font-semibold text-muted-foreground">Enter your new email address. You will be signed out of all current sessions.</DialogDescription>
            </div>
            <Input
              value={newEmail}
              onChange={e => setNewEmail(e.target.value)}
              placeholder="new.email@protocol.com"
              className="h-12 rounded-xl bg-muted/40 border-border/40 focus:ring-primary/10 text-sm font-extrabold px-5"
            />
            <Button onClick={handleChangeEmail} className="w-full h-12 rounded-xl font-extrabold text-sm shadow-xl shadow-primary/20">
              Confirm Migration
            </Button>
          </div>
        </DialogContent>
      </Dialog>

      <Dialog open={showPasswordDialog} onOpenChange={setShowPasswordDialog}>
        <DialogContent className="rounded-2xl max-w-md p-0 border-none overflow-hidden sm:mt-0 mt-20">
          <div className="p-8 sm:p-10 space-y-6 bg-card">
            <div className="text-center space-y-2">
              <div className="w-12 h-12 rounded-2xl bg-primary/10 flex items-center justify-center mx-auto mb-4">
                 <Key className="w-6 h-6 text-primary" />
              </div>
              <DialogTitle className="text-xl font-extrabold text-foreground tracking-tight">Rotate Access Keys</DialogTitle>
              <DialogDescription className="text-xs font-semibold text-muted-foreground">Maintain account integrity by updating your security phrase.</DialogDescription>
            </div>
            <div className="space-y-4">
              <Input
                type="password"
                value={oldPwd}
                onChange={e => setOldPwd(e.target.value)}
                placeholder="Current Pass-phrase"
                className="h-12 rounded-xl bg-muted/40 border-border/40 focus:ring-primary/10 text-sm font-extrabold px-5"
              />
              <Input
                type="password"
                value={newPwd}
                onChange={e => setNewPwd(e.target.value)}
                placeholder="New Access Sequence"
                className="h-12 rounded-xl bg-muted/40 border-border/40 focus:ring-primary/10 text-sm font-extrabold px-5"
              />
            </div>
            <Button onClick={handleChangePassword} className="w-full h-12 rounded-xl font-extrabold text-sm shadow-xl shadow-primary/20">
              Apply New Credentials
            </Button>
          </div>
        </DialogContent>
      </Dialog>

      <Dialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
        <DialogContent className="rounded-2xl max-w-md p-0 border-none overflow-hidden sm:mt-0 mt-20">
          <div className="p-8 sm:p-10 space-y-6 bg-card border-t-[6px] border-rose-500">
            <div className="text-center space-y-2">
              <div className="w-12 h-12 rounded-2xl bg-rose-500/10 flex items-center justify-center mx-auto mb-4">
                 <AlertCircle className="w-6 h-6 text-rose-500" />
              </div>
              <DialogTitle className="text-xl font-extrabold text-rose-600 tracking-tight leading-tight">Identity Deletion Request</DialogTitle>
              <DialogDescription className="text-xs font-semibold text-rose-600/60">This protocol will irreversibly terminate your presence and all associated match logs on RoomShare AI.</DialogDescription>
            </div>
            <Input
              type="password"
              value={delPwd}
              onChange={e => setDelPwd(e.target.value)}
              placeholder="Confirm Pass-phrase to Proceed"
              className="h-12 rounded-xl bg-rose-500/[0.03] border-rose-500/20 focus:ring-rose-500/10 text-sm font-extrabold px-5 text-rose-600"
            />
            <Button onClick={handleDeleteAccount} variant="destructive" className="w-full h-12 rounded-xl font-extrabold text-sm shadow-xl shadow-rose-500/20">
              Terminate Identity Permanently
            </Button>
            <div className="text-center">
               <button onClick={() => setShowDeleteDialog(false)} className="text-[10px] uppercase tracking-widest font-black text-muted-foreground hover:text-foreground">Abort Protocol</button>
            </div>
          </div>
        </DialogContent>
      </Dialog>

    </div>
  );
}

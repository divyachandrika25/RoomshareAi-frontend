import { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { roomAPI } from '@/lib/api';
import { 
  ArrowLeft, Upload, ShieldCheck, FileText, AlertCircle, 
  CheckCircle2, Info, ChevronRight, Lock, Eye, Trash2
} from 'lucide-react';
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import toast from 'react-hot-toast';

export default function RoomShareVerificationPage() {
  const { requestId } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const email = user?.email || '';

  const [loading, setLoading] = useState(true);
  const [uploading, setUploading] = useState(false);
  const [data, setData] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [preview, setPreview] = useState(null);
  const fileInputRef = useRef(null);

  useEffect(() => {
    if (!requestId) return;
    setLoading(true);
    roomAPI.getRoomShareVerification(requestId)
      .then(res => {
        if (res.data?.success) {
          setData(res.data.data);
          if (res.data.data.identity_document) {
            setPreview(res.data.data.identity_document);
          }
        }
      })
      .catch(err => {
        console.error(err);
        toast.error("Failed to load verification status");
      })
      .finally(() => setLoading(false));
  }, [requestId]);

  const handleFileSelect = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        toast.error("File size must be under 5MB");
        return;
      }
      setSelectedFile(file);
      const reader = new FileReader();
      reader.onloadend = () => setPreview(reader.result);
      reader.readAsDataURL(file);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) return toast.error("Please select a document");
    
    setUploading(true);
    const formData = new FormData();
    formData.append('request_id', requestId);
    formData.append('source', 'WEB');
    formData.append('identity_document', selectedFile);

    try {
      const res = await roomAPI.uploadIdentityDocument(formData);
      if (res.data?.success) {
        toast.success("Document uploaded successfully!");
        setData(prev => ({ ...prev, status: 'VERIFIED' }));
        // After small delay, move to final if status is approved/verified as per backend
        setTimeout(() => navigate(`/dashboard/room-share/final-review/${requestId}`), 1000);
      }
    } catch (err) {
      toast.error(err.response?.data?.message || "Upload failed");
    } finally {
      setUploading(false);
    }
  };

  if (loading) return (
    <div className="flex items-center justify-center min-h-[60vh]">
      <div className="w-10 h-10 border-3 border-primary/20 border-t-primary rounded-full animate-spin" />
    </div>
  );

  return (
    <div className="max-w-3xl mx-auto px-4 py-8 space-y-8">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button 
            variant="ghost" 
            size="icon" 
            onClick={() => navigate(-1)}
            className="rounded-full h-10 w-10 border border-border/50 bg-card hover:bg-muted"
          >
            <ArrowLeft className="w-5 h-5" />
          </Button>
          <div>
            <h1 className="text-2xl font-black text-foreground tracking-tight">Identity Verification</h1>
            <p className="text-sm text-muted-foreground font-medium">Step 2 of 3: Secure Document Upload</p>
          </div>
        </div>
        <Badge variant="outline" className="bg-primary/5 text-primary border-primary/20 px-3 py-1 font-black rounded-lg">
           {data?.status || "PENDING"}
        </Badge>
      </div>

      <div className="grid md:grid-cols-[1fr,280px] gap-8">
        <div className="space-y-6">
          <Card className="p-8 border-none shadow-2xl shadow-primary/5 bg-card/50 backdrop-blur-md">
            <div className="space-y-6">
              <div className="text-center space-y-2">
                 <div className="w-16 h-16 rounded-3xl bg-primary/10 flex items-center justify-center mx-auto mb-4 border border-primary/10">
                    <ShieldCheck className="w-8 h-8 text-primary" />
                 </div>
                 <h3 className="text-xl font-black text-foreground">Upload your ID Card</h3>
                 <p className="text-xs text-muted-foreground font-medium max-w-sm mx-auto leading-relaxed">
                   To keep our community safe, we require all room share applicants to verify their identity. We accept Passports, Aadhaar, or Driver's Licenses.
                 </p>
              </div>

              {/* Upload Dropzone */}
              <div 
                onClick={() => !uploading && fileInputRef.current?.click()}
                className={`relative group cursor-pointer border-2 border-dashed rounded-3xl p-6 transition-all duration-300 min-h-[240px] flex flex-col items-center justify-center text-center ${
                  preview ? 'border-primary/50 bg-primary/5' : 'border-border/60 hover:border-primary/40 hover:bg-muted'
                }`}
              >
                <input 
                  type="file" 
                  ref={fileInputRef} 
                  onChange={handleFileSelect} 
                  className="hidden" 
                  accept="image/*,.pdf" 
                />

                {preview ? (
                  <div className="space-y-4 w-full">
                     <div className="relative w-48 h-32 mx-auto rounded-xl overflow-hidden shadow-2xl border border-white/20">
                        <img src={preview} className="w-full h-full object-cover" alt="Preview" />
                        <div className="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-2">
                           <Button size="icon" variant="secondary" className="h-8 w-8 rounded-full">
                              <Eye className="w-4 h-4" />
                           </Button>
                           <Button 
                             size="icon" 
                             variant="destructive" 
                             className="h-8 w-8 rounded-full"
                             onClick={(e) => { e.stopPropagation(); setPreview(null); setSelectedFile(null); }}
                           >
                              <Trash2 className="w-4 h-4" />
                           </Button>
                        </div>
                     </div>
                     <p className="text-sm font-black text-primary">Document Ready to Upload</p>
                  </div>
                ) : (
                  <div className="space-y-3">
                    <div className="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center mx-auto group-hover:scale-110 transition-transform">
                       <Upload className="w-6 h-6 text-primary" />
                    </div>
                    <div>
                      <p className="text-sm font-extrabold text-foreground">Click or Drag to Upload</p>
                      <p className="text-[10px] text-muted-foreground font-bold tracking-widest mt-1">MAX 5MB (JPG, PNG)</p>
                    </div>
                  </div>
                )}
              </div>

              <div className="flex items-center gap-3 p-4 bg-muted/30 rounded-2xl border border-border/40">
                <Lock className="w-4 h-4 text-muted-foreground" />
                <p className="text-[10px] sm:text-xs text-muted-foreground font-semibold leading-relaxed shrink">
                   Your document is encrypted and stored securely. It is only visible to the RoomShare verification team.
                </p>
              </div>

              <Button 
                onClick={handleUpload}
                disabled={uploading || !selectedFile}
                className="w-full h-14 rounded-2xl font-black text-sm uppercase tracking-widest shadow-lg shadow-primary/20 group hover:-translate-y-0.5 transition-all"
              >
                {uploading ? "Uploading Security File..." : "Confirm & Send for Verification"}
                {!uploading && <ChevronRight className="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" />}
              </Button>
            </div>
          </Card>
        </div>

        <div className="space-y-6">
           <div className="space-y-4">
              <h4 className="text-sm font-black text-foreground uppercase tracking-widest px-2">Privacy & Security</h4>
              <Card className="p-4 space-y-4 border-none bg-card shadow-lg shadow-primary/5">
                 {[
                   { icon: ShieldCheck, title: "Identity Protection", desc: "Your real ID is never shared with other users." },
                   { icon: FileText, title: "Verified Badge", desc: "Get the prestigious verified badge on your profile." },
                   { icon: CheckCircle2, title: "Host Trust", desc: "Verified profiles are 80% more likely to be accepted." }
                 ].map((item, i) => (
                   <div key={i} className="space-y-1">
                      <div className="flex items-center gap-2">
                         <item.icon className="w-3 h-3 text-primary" />
                         <span className="text-[10px] font-black text-foreground uppercase tracking-wider">{item.title}</span>
                      </div>
                      <p className="text-[11px] text-muted-foreground font-bold leading-normal pl-5">{item.desc}</p>
                   </div>
                 ))}
              </Card>
           </div>

           <div className="bg-amber-500/10 rounded-2xl p-4 border border-amber-500/20">
              <div className="flex items-center gap-2 mb-2">
                 <AlertCircle className="w-4 h-4 text-amber-500" />
                 <h4 className="text-[10px] font-black text-amber-600 uppercase tracking-widest">Notice</h4>
              </div>
              <p className="text-[11px] text-amber-700/80 font-bold leading-normal italic">
                Selfies or unclear documents will be rejected. Ensure your name matches your profile.
              </p>
           </div>
        </div>
      </div>
    </div>
  );
}

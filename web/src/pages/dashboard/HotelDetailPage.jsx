import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { hotelAPI } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';
import toast from 'react-hot-toast';
import {
  ArrowLeft, Star, MapPin, Phone, Globe, Mail,
  BedDouble, Users, CheckCircle2, XCircle, Calendar as CalendarIcon,
  Loader2, ExternalLink, Wifi, Coffee, Car, Dumbbell, Waves
} from 'lucide-react';
import { format } from "date-fns";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";

export default function HotelDetailPage() {
  const { hotelId } = useParams();
  const location = useLocation();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [hotel, setHotel] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isExternal, setIsExternal] = useState(false);
  const [checkIn, setCheckIn] = useState('');
  const [checkOut, setCheckOut] = useState('');
  const [bookingRoom, setBookingRoom] = useState(null);
  const [guests, setGuests] = useState(1);
  const [specialReqs, setSpecialReqs] = useState('');
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if ((hotelId.startsWith('external-') || hotelId.startsWith('ext-')) && location.state?.externalHotel) {
      const ext = location.state.externalHotel;
      setHotel({
        ...ext,
        name: ext.title,
        rating: ext.rating || null,
        review_count: ext.review_count || 0,
        amenities: ext.amenities || [],
        image_url: ext.image || null,
        rooms: ext.rooms || [
          {
            id: 'ext-room',
            room_number: ext.room_number || '—',
            room_type: ext.type || 'Room',
            capacity: ext.capacity || 2,
            price_per_night: ext.price,
            bed_type: ext.bed_type || '—',
            floor: ext.floor || 1,
            is_available: true,
            amenities: ext.room_amenities || []
          }
        ]
      });
      setIsExternal(true);
      setLoading(false);
    } else {
      fetchHotel();
    }
  }, [hotelId]);

  const fetchHotel = async () => {
    try {
      setLoading(true);
      const res = await hotelAPI.getDetail(hotelId, checkIn, checkOut);
      setHotel(res.data);
      setIsExternal(false);
    } catch (e) {
      console.error(e);
      toast.error('Failed to load hotel');
    } finally {
      setLoading(false);
    }
  };

  const checkAvailability = () => {
    if (!checkIn || !checkOut) { toast.error('Please select check-in and check-out dates'); return; }
    if (new Date(checkOut) <= new Date(checkIn)) { toast.error('Check-out must be after check-in'); return; }
    fetchHotel();
    toast.success('Availability updated!');
  };

  const handleBook = (room) => {
    if (!checkIn || !checkOut) { toast.error('Please select dates first'); return; }
    setBookingRoom(room);
  };

  const confirmBooking = async () => {
    if (!bookingRoom) return;
    try {
      setSubmitting(true);
      const res = await hotelAPI.bookRoom({
        email: user?.email,
        room_id: bookingRoom.id,
        check_in: checkIn,
        check_out: checkOut,
        guests,
        special_requests: specialReqs,
        hotel_name: hotel.name,
        hotel_address: hotel.address,
        price: bookingRoom.price_per_night
      });
      toast.success(isExternal
        ? 'External booking saved to your history!'
        : `Booked! Ref: ${res.data.booking_reference}`
      );
      setBookingRoom(null);
      setSpecialReqs('');
      if (!isExternal) fetchHotel();
    } catch (e) {
      toast.error(e.response?.data?.error || 'Booking failed');
    } finally {
      setSubmitting(false);
    }
  };

  const getNights = () => {
    if (!checkIn || !checkOut) return 0;
    const d = (new Date(checkOut) - new Date(checkIn)) / (1000 * 60 * 60 * 24);
    return d > 0 ? d : 0;
  };

  if (loading) return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
      <div className="w-12 h-12 rounded-full border-2 border-primary/20 border-t-primary animate-spin" />
      <p className="text-sm text-muted-foreground animate-pulse">Loading hotel details…</p>
    </div>
  );

  if (!hotel) return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] gap-3">
      <BedDouble size={48} className="text-muted-foreground/30" />
      <p className="text-muted-foreground font-medium">Hotel not found</p>
      <button onClick={() => navigate('/dashboard/hotels')}
        className="text-sm text-primary hover:underline">← Back to Hotels</button>
    </div>
  );

  const nights = getNights();

  return (
    <div className="max-w-6xl mx-auto px-3 sm:px-5 lg:px-0 pb-20 space-y-8 sm:space-y-10">

      {/* Back Nav */}
      <button
        onClick={() => navigate('/dashboard/hotels')}
        className="inline-flex items-center gap-2 text-sm font-medium text-muted-foreground hover:text-foreground transition-colors group"
      >
        <span className="group-hover:-translate-x-0.5 transition-transform">
          <ArrowLeft size={16} />
        </span>
        Back to Hotels
      </button>

      {/* Hero Card */}
      <div className="bg-card rounded-2xl border border-border overflow-hidden shadow-sm">
        {/* Image */}
        <div className="relative h-48 sm:h-64 lg:h-80 bg-muted">
          {hotel.image_url ? (
            <img
              src={hotel.image_url}
              alt={hotel.name}
              className="w-full h-full object-cover"
            />
          ) : (
            <div className="w-full h-full flex flex-col items-center justify-center text-muted-foreground/30">
              <BedDouble size={56} />
              <p className="text-sm mt-2">No image available</p>
            </div>
          )}
          {isExternal && (
            <span className="absolute top-3 right-3 bg-amber-500 text-white text-xs font-semibold px-2.5 py-1 rounded-full shadow">
              External Listing
            </span>
          )}
        </div>

        {/* Details */}
        <div className="p-5 sm:p-6 lg:p-8">
          <div className="flex flex-col sm:flex-row sm:items-start sm:justify-between gap-3">
            <div className="flex-1 min-w-0">
              <h1 className="text-xl sm:text-2xl lg:text-3xl font-bold text-foreground leading-tight">
                {hotel.name}
              </h1>
              {hotel.address && (
                <div className="flex items-center gap-1.5 mt-1.5 text-sm text-muted-foreground">
                  <MapPin size={14} className="shrink-0" />
                  <span className="truncate">{hotel.address}</span>
                </div>
              )}
            </div>

            {/* Rating Badge */}
            {hotel.rating && (
              <div className="shrink-0 flex flex-col items-end gap-1">
                <span className="flex items-center gap-1.5 bg-amber-500/10 text-amber-500 px-3 py-1.5 rounded-xl text-sm font-bold">
                  <Star size={14} className="fill-amber-500" />
                  {hotel.rating}
                </span>
                {hotel.review_count > 0 && (
                  <span className="text-xs text-muted-foreground">
                    {hotel.review_count.toLocaleString()} reviews
                  </span>
                )}
              </div>
            )}
          </div>

          {/* Star rating */}
          {hotel.stars && (
            <div className="flex items-center gap-0.5 mt-2">
              {Array.from({ length: 5 }, (_, i) => (
                <Star
                  key={i}
                  size={14}
                  className={i < Math.round(hotel.stars)
                    ? 'fill-amber-400 text-amber-400'
                    : 'text-muted-foreground/20'}
                />
              ))}
              <span className="text-xs text-muted-foreground ml-1.5">{hotel.stars}-star hotel</span>
            </div>
          )}

          {hotel.description && (
            <p className="text-sm text-muted-foreground mt-4 leading-relaxed max-w-3xl">
              {hotel.description}
            </p>
          )}

          {/* Contact Info */}
          {(hotel.phone || hotel.email || hotel.website) && (
            <div className="flex flex-wrap gap-x-5 gap-y-2 mt-5 pt-4 border-t border-border">
              {hotel.phone && (
                <a href={`tel:${hotel.phone}`} className="flex items-center gap-1.5 text-sm text-muted-foreground hover:text-foreground transition-colors">
                  <Phone size={14} /> {hotel.phone}
                </a>
              )}
              {hotel.email && (
                <a href={`mailto:${hotel.email}`} className="flex items-center gap-1.5 text-sm text-muted-foreground hover:text-foreground transition-colors">
                  <Mail size={14} /> {hotel.email}
                </a>
              )}
              {hotel.website && (
                <a href={hotel.website} target="_blank" rel="noreferrer"
                  className="flex items-center gap-1.5 text-sm text-primary hover:underline">
                  <Globe size={14} /> Website
                  <ExternalLink size={11} />
                </a>
              )}
            </div>
          )}

          {/* Amenities */}
          {hotel.amenities?.length > 0 && (
            <div className="flex flex-wrap gap-2 mt-4">
              {hotel.amenities.map((amenity, i) => (
                <span
                  key={i}
                  className="bg-primary/8 text-primary text-xs px-3 py-1.5 rounded-lg font-medium border border-primary/10"
                >
                  {amenity.trim()}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Date / Guest Selector */}
      <div className="bg-card rounded-2xl border border-border p-5 sm:p-6 shadow-sm">
        <h2 className="text-base sm:text-lg font-semibold text-foreground mb-4 flex items-center gap-2">
          <CalendarIcon size={18} className="text-primary" />
          Select Dates & Guests
        </h2>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3 items-end">
          {/* Check-in */}
          <div className="flex flex-col gap-1.5">
            <label className="text-xs font-medium text-muted-foreground">Check-in</label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className={cn(
                    "w-full justify-start text-left font-medium rounded-xl h-11 text-sm",
                    !checkIn && "text-muted-foreground"
                  )}
                >
                  <CalendarIcon className="mr-2 h-4 w-4 shrink-0" />
                  {checkIn ? format(new Date(checkIn), "MMM d, yyyy") : "Pick a date"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0 rounded-xl" align="start">
                <Calendar
                  mode="single"
                  selected={checkIn ? new Date(checkIn) : undefined}
                  onSelect={(date) => {
                    const d = date ? format(date, "yyyy-MM-dd") : "";
                    setCheckIn(d);
                    if (checkOut && date && new Date(checkOut) <= date) setCheckOut("");
                  }}
                  disabled={(date) => date < new Date(new Date().setHours(0, 0, 0, 0))}
                  initialFocus
                  className="rounded-xl border-none"
                />
              </PopoverContent>
            </Popover>
          </div>

          {/* Check-out */}
          <div className="flex flex-col gap-1.5">
            <label className="text-xs font-medium text-muted-foreground">Check-out</label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className={cn(
                    "w-full justify-start text-left font-medium rounded-xl h-11 text-sm",
                    !checkOut && "text-muted-foreground"
                  )}
                >
                  <CalendarIcon className="mr-2 h-4 w-4 shrink-0" />
                  {checkOut ? format(new Date(checkOut), "MMM d, yyyy") : "Pick a date"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0 rounded-xl" align="start">
                <Calendar
                  mode="single"
                  selected={checkOut ? new Date(checkOut) : undefined}
                  onSelect={(date) => setCheckOut(date ? format(date, "yyyy-MM-dd") : "")}
                  disabled={(date) => {
                    const minDate = checkIn ? new Date(checkIn) : new Date(new Date().setHours(0, 0, 0, 0));
                    return date <= minDate;
                  }}
                  initialFocus
                  className="rounded-xl border-none"
                />
              </PopoverContent>
            </Popover>
          </div>

          {/* Guests */}
          <div className="flex flex-col gap-1.5">
            <label className="text-xs font-medium text-muted-foreground">Guests</label>
            <select
              value={guests}
              onChange={(e) => setGuests(Number(e.target.value))}
              className="w-full px-3 h-11 rounded-xl bg-background border border-input text-foreground focus:border-primary outline-none text-sm font-medium transition-colors"
            >
              {[1, 2, 3, 4, 5, 6].map(n => (
                <option key={n} value={n}>{n} {n > 1 ? 'Guests' : 'Guest'}</option>
              ))}
            </select>
          </div>

          {/* CTA */}
          <button
            onClick={checkAvailability}
            className="h-11 px-5 bg-primary text-primary-foreground rounded-xl font-semibold text-sm hover:bg-primary/90 transition-colors flex items-center justify-center gap-2 shadow-sm"
          >
            Check Availability
          </button>
        </div>

        {nights > 0 && (
          <div className="mt-3 flex items-center gap-2 text-sm text-muted-foreground bg-muted/50 rounded-lg px-3 py-2 w-fit">
            <CalendarIcon size={13} className="text-primary" />
            <span>
              <span className="font-semibold text-foreground">{nights} night{nights > 1 ? 's' : ''}</span>
              {' '}· {format(new Date(checkIn), 'MMM d')} → {format(new Date(checkOut), 'MMM d, yyyy')}
            </span>
          </div>
        )}
      </div>

      {/* Available Rooms */}
      <div>
        <h2 className="text-base sm:text-lg font-semibold text-foreground mb-3">
          {hotel.rooms?.length
            ? `${hotel.rooms.length} Room${hotel.rooms.length > 1 ? 's' : ''} Available`
            : 'Rooms'
          }
        </h2>

        {!hotel.rooms?.length ? (
          <div className="bg-card rounded-2xl border border-border p-10 text-center text-muted-foreground">
            <BedDouble size={36} className="mx-auto mb-2 opacity-30" />
            <p className="text-sm">No rooms available for the selected dates.</p>
          </div>
        ) : (
          <div className="space-y-3">
            {hotel.rooms.map((room) => (
              <div
                key={room.id}
                className={cn(
                  "bg-card rounded-2xl border p-5 transition-all shadow-sm",
                  room.is_available
                    ? "border-border hover:border-primary/30 hover:shadow-md"
                    : "border-destructive/20 opacity-60"
                )}
              >
                <div className="flex flex-col sm:flex-row sm:items-center gap-4 sm:gap-6">
                  {/* Room Info */}
                  <div className="flex-1 min-w-0">
                    <div className="flex flex-wrap items-center gap-2">
                      <h3 className="font-semibold text-foreground text-sm sm:text-base">
                        Room {room.room_number}
                      </h3>
                      <span className="bg-primary/10 text-primary text-xs px-2 py-0.5 rounded-md font-semibold border border-primary/15">
                        {room.room_type}
                      </span>
                    </div>
                    <div className="flex flex-wrap items-center gap-3 sm:gap-4 mt-1.5 text-xs sm:text-sm text-muted-foreground">
                      {room.bed_type && (
                        <span className="flex items-center gap-1">
                          <BedDouble size={13} /> {room.bed_type}
                        </span>
                      )}
                      <span className="flex items-center gap-1">
                        <Users size={13} /> Up to {room.capacity} guests
                      </span>
                      {room.floor && (
                        <span>Floor {room.floor}</span>
                      )}
                    </div>
                    {room.amenities?.length > 0 && (
                      <div className="flex flex-wrap gap-1 mt-2">
                        {room.amenities.map((a, i) => (
                          <span key={i} className="text-xs bg-secondary text-muted-foreground px-2 py-0.5 rounded-md">
                            {a.trim()}
                          </span>
                        ))}
                      </div>
                    )}
                  </div>

                  {/* Price + Action */}
                  <div className="flex sm:flex-col items-center sm:items-end justify-between sm:justify-start gap-3 sm:gap-1 pt-3 sm:pt-0 border-t sm:border-t-0 border-border">
                    <div className="text-left sm:text-right">
                      <p className="text-xl sm:text-2xl font-bold text-foreground leading-none">
                        ₹{room.price_per_night?.toLocaleString()}
                      </p>
                      <p className="text-xs text-muted-foreground mt-0.5">per night</p>
                      {nights > 0 && (
                        <p className="text-sm font-semibold text-primary mt-1">
                          ₹{(room.price_per_night * nights).toLocaleString()} total
                        </p>
                      )}
                    </div>

                    <div>
                      {isExternal ? (
                        <div className="flex items-center gap-2">
                          {hotel.website && (
                            <button
                              onClick={() => window.open(hotel.website, '_blank')}
                              className="flex items-center gap-1.5 px-3 py-2 bg-secondary text-muted-foreground border border-border rounded-xl text-xs font-medium hover:bg-muted transition-colors"
                            >
                              <ExternalLink size={13} /> View Site
                            </button>
                          )}
                          <button
                            onClick={() => handleBook(room)}
                            className="flex items-center gap-1.5 px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-xs sm:text-sm font-semibold transition-colors shadow-sm"
                          >
                            <CheckCircle2 size={15} /> Book
                          </button>
                        </div>
                      ) : room.is_available ? (
                        <button
                          onClick={() => handleBook(room)}
                          className="flex items-center gap-1.5 px-4 sm:px-5 py-2 sm:py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-xs sm:text-sm font-semibold transition-colors shadow-sm whitespace-nowrap"
                        >
                          <CheckCircle2 size={15} /> Book Now
                        </button>
                      ) : (
                        <span className="flex items-center gap-1.5 px-4 py-2 bg-destructive/8 text-destructive rounded-xl text-xs sm:text-sm font-semibold border border-destructive/15">
                          <XCircle size={15} /> Booked
                        </span>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Booking Confirmation Modal */}
      {bookingRoom && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-end sm:items-center justify-center p-0 sm:p-4">
          <div className="bg-card w-full sm:max-w-md sm:rounded-2xl rounded-t-2xl p-6 shadow-2xl border border-border max-h-[90vh] overflow-y-auto">
            <div className="flex items-center justify-between mb-5">
              <h3 className="text-lg font-bold text-foreground">Confirm Booking</h3>
              <button
                onClick={() => setBookingRoom(null)}
                className="text-muted-foreground hover:text-foreground transition-colors p-1"
              >
                <XCircle size={20} />
              </button>
            </div>

            {/* Summary */}
            <div className="bg-muted/40 rounded-xl p-4 space-y-2.5 text-sm">
              {[
                { label: 'Hotel', value: hotel.name },
                { label: 'Room', value: `${bookingRoom.room_number} · ${bookingRoom.room_type}` },
                { label: 'Check-in', value: checkIn ? format(new Date(checkIn), 'EEEE, MMM d yyyy') : '—' },
                { label: 'Check-out', value: checkOut ? format(new Date(checkOut), 'EEEE, MMM d yyyy') : '—' },
                { label: 'Duration', value: `${nights} night${nights > 1 ? 's' : ''}` },
                { label: 'Guests', value: `${guests} ${guests > 1 ? 'guests' : 'guest'}` },
              ].map(({ label, value }) => (
                <div key={label} className="flex justify-between items-start gap-3">
                  <span className="text-muted-foreground shrink-0">{label}</span>
                  <span className="font-medium text-foreground text-right">{value}</span>
                </div>
              ))}
              <div className="border-t border-border pt-2.5 flex justify-between items-center">
                <span className="font-bold text-foreground">Total</span>
                <span className="text-lg font-bold text-primary">
                  ₹{(bookingRoom.price_per_night * nights).toLocaleString()}
                </span>
              </div>
            </div>

            {/* Special Requests */}
            <div className="mt-4">
              <label className="text-xs font-medium text-muted-foreground block mb-1.5">
                Special Requests <span className="font-normal">(optional)</span>
              </label>
              <textarea
                value={specialReqs}
                onChange={(e) => setSpecialReqs(e.target.value)}
                placeholder="e.g. extra pillows, early check-in, high floor…"
                className="w-full px-3 py-2.5 rounded-xl bg-background border border-input text-foreground placeholder:text-muted-foreground focus:border-primary outline-none text-sm resize-none h-20 transition-colors"
              />
            </div>

            {/* Actions */}
            <div className="flex gap-3 mt-5">
              <button
                onClick={() => setBookingRoom(null)}
                className="flex-1 py-2.5 rounded-xl border border-border text-muted-foreground font-medium text-sm hover:bg-muted transition-colors"
              >
                Cancel
              </button>
              <button
                onClick={confirmBooking}
                disabled={submitting}
                className="flex-1 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-700 disabled:opacity-60 text-white font-semibold text-sm transition-colors flex items-center justify-center gap-2 shadow-sm"
              >
                {submitting
                  ? <><Loader2 size={15} className="animate-spin" /> Booking…</>
                  : <><CheckCircle2 size={15} /> Confirm & Pay</>
                }
              </button>
            </div>

            {user?.email && (
              <p className="text-xs text-muted-foreground text-center mt-3">
                Confirmation will be sent to <span className="font-medium text-foreground">{user.email}</span>
              </p>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
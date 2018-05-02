# set terminal pngcairo  transparent enhanced font "arial,10" fontscale 1.0 size 600, 400 
# set output 'cerf.3.png'
unset key
set view map scale 1
set samples 200, 200
set isosamples 200, 200
set contour base
set cntrlabel onecolor format '%8.3g' font '' start 5 interval 20
set cntrparam levels discrete 0.1,0.2 ,0.5 ,1 ,2 ,5 ,10 ,20 ,50 ,100 ,200 ,500 
set size ratio 1 1,1
set xtics border in scale 0,0 mirror norotate  autojustify
set ytics border in scale 0,0 mirror norotate  autojustify
set ztics border in scale 0,0 nomirror norotate  autojustify
set cbtics border in scale 0,0 mirror norotate  autojustify
set cbtics  norangelimit 
set cbtics   ("0" -3.14159, "2π" 3.14159)
set rtics axis in scale 0,0 nomirror norotate  autojustify
set title "Complex error function cerf( x + iy )" 
set urange [ -3.00000 : 3.00000 ] noreverse nowriteback
set vrange [ -3.00000 : 3.00000 ] noreverse nowriteback
set cblabel "Phase Angle" 
set cblabel  offset character -2, 0, 0 font "" textcolor lt -1 rotate
set cbrange [ -3.14159 : 3.14159 ] noreverse nowriteback
set bmargin at screen 0.1
set palette positive nops_allcF maxcolors 0 gamma 1.5 color model HSV 
set palette defined ( 0 0 1 1, 1 1 1 1 )
Hue(x,y) = (pi + atan2(-y,-x)) / (2*pi)
phase(x,y) = hsv2rgb( Hue(x,y), sqrt(x**2+y**2), 1. )
rp(x,y) = real(f(x,y))
f(x,y) = cerf(x+y*{0,1})
ip(x,y) = imag(f(x,y))
color(x,y) = hsv2rgb( Hue( rp(x,y), ip(x,y) ), abs(f(x,y)), 1. )
save_encoding = "utf8"
DEBUG_TERM_HTIC = 119
DEBUG_TERM_VTIC = 119
GPFUN_Hue = "Hue(x,y) = (pi + atan2(-y,-x)) / (2*pi)"
GPFUN_phase = "phase(x,y) = hsv2rgb( Hue(x,y), sqrt(x**2+y**2), 1. )"
GPFUN_rp = "rp(x,y) = real(f(x,y))"
GPFUN_ip = "ip(x,y) = imag(f(x,y))"
GPFUN_color = "color(x,y) = hsv2rgb( Hue( rp(x,y), ip(x,y) ), abs(f(x,y)), 1. )"
GPFUN_f = "f(x,y) = cerf(x+y*{0,1})"
## Last datafile plotted: "++"
splot '++' using 1:2:(color($1,$2)) with pm3d lc rgb variable nocontour,       '++' using 1:2:(abs(cerf($1+$2*{0,1}))) with lines nosurf lt -1

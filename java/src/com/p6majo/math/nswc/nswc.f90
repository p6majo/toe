!
program hello
    real*8 zcomp(2)
    real*8 wcomp(2)
    real*8 nucomp(2)
    ! elliptic function
    real*8 ucomp(2)
    !real*4 pcomp(2)
    real*8 scomp(2)
    real*8 ccomp(2)
    real*8 dcomp(2)
    real*4 k,l
    integer*4 err
    !
    ! gamma function
    zcomp(1)=6.
    zcomp(2)=0.
    nucomp(1)=1.
    nucomp(2)=0.
    print *,"gamma function computation"
    call mycgamma(zcomp,wcomp)
    print *,wcomp
    ! bessel
    print *,"bessel function computation"
    call mycbesselj(nucomp,zcomp,wcomp)
    print *,wcomp
    call mybesselj(1,zcomp,wcomp)
    print *,wcomp
    ! elliptic function
    ucomp(1)=1.1
    ucomp(2)=0.5
    k=0.6
    l=0.8
    !pcomp(1)=k
    !pcomp(2)=l
    print*,"elliptic function calculation"
    call myellipticcomplex(ucomp,k,l,scomp,ccomp,dcomp,err)
    print *,scomp
    print *,ccomp
    print *,dcomp
    print *,err
end program hello
!
subroutine mycloggamma(zcomp,wcomp)
    real*8 zcomp(2)
    real*8 wcomp(2)
    complex z
    complex w
    !
    z = cmplx(zcomp(1),zcomp(2))
    w = Cmplx(wcomp(1),wcomp(2))
    call CGAMMA(1,z,w)
    wcomp(1)=real(w)
    wcomp(2)=imag(w)
    return
end
!
!
subroutine mycgamma(zcomp,wcomp)
    real*8 zcomp(2)
    real*8 wcomp(2)
    complex z
    complex w
    !
    z = cmplx(zcomp(1),zcomp(2))
    w = Cmplx(wcomp(1),wcomp(2))
    call CGAMMA(0,z,w)
    wcomp(1)=real(w)
    wcomp(2)=imag(w)
    return
end
!
subroutine mycbesselj(nucomp,zcomp,wcomp)
    real*8 zcomp(2)
    real*8 wcomp(2)
    real*8 nucomp(2)
    complex nu,z,w
    !
    nu = cmplx(nucomp(1),nucomp(2))
    z = cmplx(zcomp(1),zcomp(2))
    w = cmplx(wcomp(1),wcomp(2))
    !
    call CBSSLJ(z,nu,w)
    wcomp(1)=real(w)
    wcomp(2)=imag(w)
    return
    end
!
subroutine mybesselj(nu,zcomp,wcomp)
    integer*4 nu
    real*8 zcomp(2)
    real*8 wcomp(2)
    complex z,w
    z = cmplx(zcomp(1),zcomp(2))
    w = cmplx(wcomp(1),wcomp(2))
    call BSSLJ(z,nu,w)
    wcomp(1)=real(w)
    wcomp(2)=imag(w)
    return
    end
    !
subroutine myelliptic(u,p,s,c,d,err)
    integer*4 err
    real*4 u,s,c,d
    real*4 p(2),k,l
    k=p(1)
    l=p(2)
    call ellpf(u,k,l,s,c,d,err)
    return
end
!
subroutine myellipticcomplex(ucomp,k,l,scomp,ccomp,dcomp,err)
    integer*4 err
    real*4 k,l
    real*8 ucomp(2)
    !real*4 pcomp(2)
    real*8 scomp(2)
    real*8 ccomp(2)
    real*8 dcomp(2)
    complex u,s,c,d
    u = cmplx(ucomp(1),ucomp(2))
    s = cmplx(scomp(1),scomp(2))
    c = cmplx(ccomp(1),ccomp(2))
    d = cmplx(dcomp(1),dcomp(2))
    !k = pcomp(1)
    ! = pcomp(2)
    call ELPFC1(u,k,l,s,c,d,err)
    scomp(1)=real(s)
    scomp(2)=imag(s)
    ccomp(1)=real(c)
    ccomp(2)=imag(c)
    dcomp(1)=real(d)
    dcomp(2)=imag(d)
    return
end
!
!
INTEGER FUNCTION IPMPAR (I)
    !-----------------------------------------------------------------------
    !
    !IPMPAR PROVIDES THE INTEGER MACHINE CONSTANTS FOR THE COMPUTER
    !THAT IS USED. IT IS ASSUMED THAT THE ARGUMENT I IS AN INTEGER
    !HAVING ONE OF THE VALUES 1-10. IPMPAR(I) HAS THE VALUE ...
    !
    !  INTEGERS.
    !
    !ASSUME INTEGERS ARE REPRESENTED IN THE N-DIGIT, BASE-A FORM
    !
    !          SIGN ( X(N-1)*A**(N-1) + ... + X(1)*A + X(0) )
    !
    !          WHERE 0 .LE. X(I) .LT. A FOR I=0,...,N-1.
    !
    !IPMPAR(1) = A, THE BASE.
    !
    !IPMPAR(2) = N, THE NUMBER OF BASE-A DIGITS.
    !
    !IPMPAR(3) = A**N - 1, THE LARGEST MAGNITUDE.
    !
    !  FLOATING-POINT NUMBERS.
    !
    !IT IS ASSUMED THAT THE SINGLE AND DOUBLE PRECISION FLOATING
    !POINT ARITHMETICS HAVE THE SAME BASE, SAY B, AND THAT THE
    !NONZERO NUMBERS ARE REPRESENTED IN THE FORM
    !
    !          SIGN (B**E) * (X(1)/B + ... + X(M)/B**M)
    !
    !          WHERE X(I) = 0,1,...,B-1 FOR I=1,...,M,
    !          X(1) .GE. 1, AND EMIN .LE. E .LE. EMAX.
    !
    !IPMPAR(4) = B, THE BASE.
    !
    !  SINGLE-PRECISION
    !
    !IPMPAR(5) = M, THE NUMBER OF BASE-B DIGITS.
    !
    !IPMPAR(6) = EMIN, THE SMALLEST EXPONENT E.
    !
    !IPMPAR(7) = EMAX, THE LARGEST EXPONENT E.
    !
    !  DOUBLE-PRECISION
    !
    !IPMPAR(8) = M, THE NUMBER OF BASE-B DIGITS.
    !
    !IPMPAR(9) = EMIN, THE SMALLEST EXPONENT E.
    !
    !IPMPAR(10) = EMAX, THE LARGEST EXPONENT E.
    !
    !-----------------------------------------------------------------------
    !
    !TO DEFINE THIS FUNCTION FOR THE COMPUTER BEING USED, ACTIVATE
    !THE DATA STATMENTS FOR THE COMPUTER BY REMOVING THE C FROM
    !COLUMN 1. (ALL THE OTHER DATA STATEMENTS SHOULD HAVE C IN
    !COLUMN 1.)
    !
    !IF DATA STATEMENTS ARE NOT GIVEN FOR THE COMPUTER BEING USED,
    !THEN THE FORTRAN MANUAL FOR THE COMPUTER NORMALLY GIVES THE
    !CONSTANTS IPMPAR(1), IPMPAR(2), AND IPMPAR(3) FOR THE INTEGER
    !ARITHMETIC. HOWEVER, HELP MAY BE NEEDED TO OBTAIN THE CONSTANTS
    !IPMPAR(4),...,IPMPAR(10) FOR THE SINGLE AND DOUBLE PRECISION
    !ARITHMETICS. THE SUBROUTINES MACH AND RADIX ARE PROVIDED FOR
    !THIS PURPOSE.
    !
    !-----------------------------------------------------------------------
    !
    !IPMPAR IS AN ADAPTATION OF THE FUNCTION I1MACH, WRITTEN BY
    !P.A. FOX, A.D. HALL, AND N.L. SCHRYER (BELL LABORATORIES).
    !IPMPAR WAS FORMED BY A.H. MORRIS (NSWC). THE CONSTANTS ARE
    !FROM BELL LABORATORIES, NSWC, AND OTHER SOURCES.
    !
    !-----------------------------------------------------------------------
    INTEGER IMACH(10)
    !
    !MACHINE CONSTANTS FOR THE ALLIANT FX/8.
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -125 /
    !DATA IMACH( 7) /   128 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1021 /
    !DATA IMACH(10) /  1024 /
    !
    !MACHINE CONSTANTS FOR THE AMDAHL MACHINES.
    !
    !DATA IMACH( 1) /    2 /
    !DATA IMACH( 2) /   31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /   16 /
    !DATA IMACH( 5) /    6 /
    !DATA IMACH( 6) /  -64 /
    !DATA IMACH( 7) /   63 /
    !DATA IMACH( 8) /   14 /
    !DATA IMACH( 9) /  -64 /
    !DATA IMACH(10) /   63 /
    !
    !MACHINE CONSTANTS FOR THE AT&T 3B SERIES, AT&T
    !PC 7300, AND AT&T 6300.
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -125 /
    !DATA IMACH( 7) /   128 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1021 /
    !DATA IMACH(10) /  1024 /
    !
    !MACHINE CONSTANTS FOR THE BURROUGHS 1700 SYSTEM.
    !
    !DATA IMACH( 1) /    2 /
    !DATA IMACH( 2) /   33 /
    !DATA IMACH( 3) / 8589934591 /
    !DATA IMACH( 4) /    2 /
    !DATA IMACH( 5) /   24 /
    !DATA IMACH( 6) / -256 /
    !DATA IMACH( 7) /  255 /
    !DATA IMACH( 8) /   60 /
    !DATA IMACH( 9) / -256 /
    !DATA IMACH(10) /  255 /
    !
    !MACHINE CONSTANTS FOR THE BURROUGHS 5700 SYSTEM.
    !
    !DATA IMACH( 1) /    2 /
    !DATA IMACH( 2) /   39 /
    !DATA IMACH( 3) / 549755813887 /
    !DATA IMACH( 4) /    8 /
    !DATA IMACH( 5) /   13 /
    !DATA IMACH( 6) /  -50 /
    !DATA IMACH( 7) /   76 /
    !DATA IMACH( 8) /   26 /
    !DATA IMACH( 9) /  -50 /
    !DATA IMACH(10) /   76 /
    !
    !MACHINE CONSTANTS FOR THE BURROUGHS 6700/7700 SYSTEMS.
    !
    !DATA IMACH( 1) /      2 /
    !DATA IMACH( 2) /     39 /
    !DATA IMACH( 3) / 549755813887 /
    !DATA IMACH( 4) /      8 /
    !DATA IMACH( 5) /     13 /
    !DATA IMACH( 6) /    -50 /
    !DATA IMACH( 7) /     76 /
    !DATA IMACH( 8) /     26 /
    !DATA IMACH( 9) / -32754 /
    !DATA IMACH(10) /  32780 /
    !
    !MACHINE CONSTANTS FOR THE CDC 6000/7000 SERIES
    !COMPUTERS, AND THE CDC CYBER 990 AND 995 (NOS
    !OPERATING SYSTEM).
    !
    !DATA IMACH( 1) /    2 /
    !DATA IMACH( 2) /   48 /
    !DATA IMACH( 3) / 281474976710655 /
    !DATA IMACH( 4) /    2 /
    !DATA IMACH( 5) /   48 /
    !DATA IMACH( 6) / -974 /
    !DATA IMACH( 7) / 1070 /
    !DATA IMACH( 8) /   95 /
    !DATA IMACH( 9) / -926 /
    !DATA IMACH(10) / 1070 /
    !
    !MACHINE CONSTANTS FOR THE CDC CYBER 990 AND 995
    !(NOS/VE OPERATING SYSTEM).
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    63 /
    !DATA IMACH( 3) / 9223372036854775807 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    48 /
    !DATA IMACH( 6) / -4096 /
    !DATA IMACH( 7) /  4095 /
    !DATA IMACH( 8) /    96 /
    !DATA IMACH( 9) / -4096 /
    !DATA IMACH(10) /  4095 /
    !
    !MACHINE CONSTANTS FOR THE CONVEX COMPUTERS
    !(NATIVE MODE).
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -127 /
    !DATA IMACH( 7) /   127 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1023 /
    !DATA IMACH(10) /  1023 /
    !
    !MACHINE CONSTANTS FOR THE CONVEX COMPUTERS
    !(IEEE MODE).
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -125 /
    !DATA IMACH( 7) /   128 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1021 /
    !DATA IMACH(10) /  1024 /
    !
    !MACHINE CONSTANTS FOR THE CRAY 2, X-MP, AND Y-MP
    !(CFT77 COMPILER USING THE 64 BIT INTEGER ARITHMETIC).
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    63 /
    !DATA IMACH( 3) / 9223372036854775807 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    48 /
    !DATA IMACH( 6) / -8188 /
    !DATA IMACH( 7) /  8189 /
    !DATA IMACH( 8) /    96 /
    !DATA IMACH( 9) / -8188 /
    !DATA IMACH(10) /  8189 /
    !
    !MACHINE CONSTANTS FOR THE CRAY 2, X-MP, AND Y-MP
    !(CFT77 COMPILER USING THE 46 BIT INTEGER ARITHMETIC).
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    46 /
    !DATA IMACH( 3) / 70368744177663 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    48 /
    !DATA IMACH( 6) / -8188 /
    !DATA IMACH( 7) /  8189 /
    !DATA IMACH( 8) /    96 /
    !DATA IMACH( 9) / -8188 /
    !DATA IMACH(10) /  8189 /
    !
    !MACHINE CONSTANTS FOR THE DATA GENERAL ECLIPSE S/200.
    !
    !DATA IMACH( 1) /    2 /
    !DATA IMACH( 2) /   15 /
    !DATA IMACH( 3) / 32767 /
    !DATA IMACH( 4) /   16 /
    !DATA IMACH( 5) /    6 /
    !DATA IMACH( 6) /  -64 /
    !DATA IMACH( 7) /   63 /
    !DATA IMACH( 8) /   14 /
    !DATA IMACH( 9) /  -64 /
    !DATA IMACH(10) /   63 /
    !
    !MACHINE CONSTANTS FOR THE HARRIS 220.
    !
    !DATA IMACH( 1) /    2 /
    !DATA IMACH( 2) /   23 /
    !DATA IMACH( 3) / 8388607 /
    !DATA IMACH( 4) /    2 /
    !DATA IMACH( 5) /   23 /
    !DATA IMACH( 6) / -127 /
    !DATA IMACH( 7) /  127 /
    !DATA IMACH( 8) /   38 /
    !DATA IMACH( 9) / -127 /
    !DATA IMACH(10) /  127 /
    !
    !MACHINE CONSTANTS FOR THE HONEYWELL 600/6000
    !AND DPS 8/70 SERIES.
    !
    !DATA IMACH( 1) /    2 /
    !DATA IMACH( 2) /   35 /
    !DATA IMACH( 3) / 34359738367 /
    !DATA IMACH( 4) /    2 /
    !DATA IMACH( 5) /   27 /
    !DATA IMACH( 6) / -127 /
    !DATA IMACH( 7) /  127 /
    !DATA IMACH( 8) /   63 /
    !DATA IMACH( 9) / -127 /
    !DATA IMACH(10) /  127 /
    !
    !MACHINE CONSTANTS FOR THE HP 9000.
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -126 /
    !DATA IMACH( 7) /   128 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1021 /
    !DATA IMACH(10) /  1024 /
    !
    !MACHINE CONSTANTS FOR THE IBM 360/370 SERIES,
    !THE IBM 3033, THE ICL 2900, THE ITEL AS/6, THE
    !XEROX SIGMA 5/7/9, AND THE SEL SYSTEMS 85/86.
    !
    !DATA IMACH( 1) /    2 /
    !DATA IMACH( 2) /   31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /   16 /
    !DATA IMACH( 5) /    6 /
    !DATA IMACH( 6) /  -64 /
    !DATA IMACH( 7) /   63 /
    !DATA IMACH( 8) /   14 /
    !DATA IMACH( 9) /  -64 /
    !DATA IMACH(10) /   63 /
    !
    !MACHINE CONSTANTS FOR THE IBM PC.
    !
    DATA IMACH( 1) /     2 /
    DATA IMACH( 2) /    31 /
    DATA IMACH( 3) / 2147483647 /
    DATA IMACH( 4) /     2 /
    DATA IMACH( 5) /    24 /
    DATA IMACH( 6) /  -125 /
    DATA IMACH( 7) /   128 /
    DATA IMACH( 8) /    53 /
    DATA IMACH( 9) / -1021 /
    DATA IMACH(10) /  1024 /
    !
    !MACHINE CONSTANTS FOR THE MACINTOSH II - ABSOFT
    !MACFORTRAN II.
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -125 /
    !DATA IMACH( 7) /   128 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1021 /
    !DATA IMACH(10) /  1024 /
    !
    !MACHINE CONSTANTS FOR THE PDP-11 FORTRAN SUPPORTING
    !32-BIT INTEGER ARITHMETIC.
    !
    !DATA IMACH( 1) /    2 /
    !DATA IMACH( 2) /   31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /    2 /
    !DATA IMACH( 5) /   24 /
    !DATA IMACH( 6) / -127 /
    !DATA IMACH( 7) /  127 /
    !DATA IMACH( 8) /   56 /
    !DATA IMACH( 9) / -127 /
    !DATA IMACH(10) /  127 /
    !
    !MACHINE CONSTANTS FOR THE SEQUENT BALANCE 8000.
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -125 /
    !DATA IMACH( 7) /   128 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1021 /
    !DATA IMACH(10) /  1024 /
    !
    !MACHINE CONSTANTS FOR THE SILICON GRAPHICS IRIS-4D
    !SERIES (MIPS R3000 PROCESSOR).
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -125 /
    !DATA IMACH( 7) /   128 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1021 /
    !DATA IMACH(10) /  1024 /
    !
    !MACHINE CONSTANTS FOR THE SUN 3.
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -125 /
    !DATA IMACH( 7) /   128 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1021 /
    !DATA IMACH(10) /  1024 /
    !
    !MACHINE CONSTANTS FOR THE UNIVAC 1100 SERIES.
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    35 /
    !DATA IMACH( 3) / 34359738367 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    27 /
    !DATA IMACH( 6) /  -128 /
    !DATA IMACH( 7) /   127 /
    !DATA IMACH( 8) /    60 /
    !DATA IMACH( 9) / -1024 /
    !DATA IMACH(10) /  1023 /
    !
    !MACHINE CONSTANTS FOR THE VAX AND MICROVAX
    !COMPUTERS - F AND D FLOATING ARITHMETICS.
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -127 /
    !DATA IMACH( 7) /   127 /
    !DATA IMACH( 8) /    56 /
    !DATA IMACH( 9) /  -127 /
    !DATA IMACH(10) /   127 /
    !
    !MACHINE CONSTANTS FOR THE VAX AND MICROVAX
    !COMPUTERS - F AND G FLOATING ARITHMETICS.
    !
    !DATA IMACH( 1) /     2 /
    !DATA IMACH( 2) /    31 /
    !DATA IMACH( 3) / 2147483647 /
    !DATA IMACH( 4) /     2 /
    !DATA IMACH( 5) /    24 /
    !DATA IMACH( 6) /  -127 /
    !DATA IMACH( 7) /   127 /
    !DATA IMACH( 8) /    53 /
    !DATA IMACH( 9) / -1023 /
    !DATA IMACH(10) /  1023 /
    !
    IPMPAR = IMACH(I)
    RETURN
END
!
REAL FUNCTION CPABS(X, Y)
    !     --------------------------------------
    !     EVALUATION OF SQRT(X*X + Y*Y)
    !     --------------------------------------
    IF (ABS(X) .LE. ABS(Y)) GO TO 10
    A = Y/X
    CPABS = ABS(X)*SQRT(1.0 + A*A)
    RETURN
    10 IF (Y .EQ. 0.0) GO TO 20
    A = X/Y
    CPABS = ABS(Y)*SQRT(1.0 + A*A)
    RETURN
    20 CPABS = 0.0
    RETURN
END
!
REAL FUNCTION SPMPAR (I)
    !-----------------------------------------------------------------------
    !
    !   SPMPAR PROVIDES THE SINGLE PRECISION MACHINE CONSTANTS FOR
    !   THE COMPUTER BEING USED. IT IS ASSUMED THAT THE ARGUMENT
    !   I IS AN INTEGER HAVING ONE OF THE VALUES 1, 2, OR 3. IF THE
    !   SINGLE PRECISION ARITHMETIC BEING USED HAS M BASE B DIGITS AND
    !   ITS SMALLEST AND LARGEST EXPONENTS ARE EMIN AND EMAX, THEN
    !
    !      SPMPAR(1) = B**(1 - M), THE MACHINE PRECISION,
    !
    !      SPMPAR(2) = B**(EMIN - 1), THE SMALLEST MAGNITUDE,
    !
    !      SPMPAR(3) = B**EMAX*(1 - B**(-M)), THE LARGEST MAGNITUDE.
    !
    !-----------------------------------------------------------------------
    !   WRITTEN BY
    !      ALFRED H. MORRIS, JR.
    !      NAVAL SURFACE WARFARE CENTER
    !      DAHLGREN VIRGINIA
    !-----------------------------------------------------------------------
    INTEGER EMIN, EMAX
    !
    IF (I .GT. 1) GO TO 10
    B = IPMPAR(4)
    M = IPMPAR(5)
    SPMPAR = B**(1 - M)
    RETURN
    !
    10 IF (I .GT. 2) GO TO 20
    B = IPMPAR(4)
    EMIN = IPMPAR(6)
    ONE = FLOAT(1)
    BINV = ONE/B
    W = B**(EMIN + 2)
    SPMPAR = ((W * BINV) * BINV) * BINV
    RETURN
    !
    20 IBETA = IPMPAR(4)
    M = IPMPAR(5)
    EMAX = IPMPAR(7)
    !
    B = IBETA
    BM1 = IBETA - 1
    ONE = FLOAT(1)
    Z = B**(M - 1)
    W = ((Z - ONE)*B + BM1)/(B*Z)
    !
    Z = B**(EMAX - 2)
    SPMPAR = ((W * Z) * B) * B
    RETURN
END
!
DOUBLE PRECISION FUNCTION DPMPAR (I)
    !-----------------------------------------------------------------------
    !
    !     DPMPAR PROVIDES THE DOUBLE PRECISION MACHINE CONSTANTS FOR
    !     THE COMPUTER BEING USED. IT IS ASSUMED THAT THE ARGUMENT
    !     I IS AN INTEGER HAVING ONE OF THE VALUES 1, 2, OR 3. IF THE
    !     DOUBLE PRECISION ARITHMETIC BEING USED HAS M BASE B DIGITS AND
    !     ITS SMALLEST AND LARGEST EXPONENTS ARE EMIN AND EMAX, THEN
    !
    !        DPMPAR(1) = B**(1 - M), THE MACHINE PRECISION,
    !
    !        DPMPAR(2) = B**(EMIN - 1), THE SMALLEST MAGNITUDE,
    !
    !        DPMPAR(3) = B**EMAX*(1 - B**(-M)), THE LARGEST MAGNITUDE.
    !
    !-----------------------------------------------------------------------
    !     WRITTEN BY
    !        ALFRED H. MORRIS, JR.
    !        NAVAL SURFACE WARFARE CENTER
    !        DAHLGREN VIRGINIA
    !-----------------------------------------------------------------------
    INTEGER EMIN, EMAX
    DOUBLE PRECISION B, BINV, BM1, ONE, W, Z
    !
    IF (I .GT. 1) GO TO 10
    B = IPMPAR(4)
    M = IPMPAR(8)
    DPMPAR = B**(1 - M)
    RETURN
    !
    10 IF (I .GT. 2) GO TO 20
    B = IPMPAR(4)
    EMIN = IPMPAR(9)
    ONE = FLOAT(1)
    BINV = ONE/B
    W = B**(EMIN + 2)
    DPMPAR = ((W * BINV) * BINV) * BINV
    RETURN
    !
    20 IBETA = IPMPAR(4)
    M = IPMPAR(8)
    EMAX = IPMPAR(10)
    !
    B = IBETA
    BM1 = IBETA - 1
    ONE = FLOAT(1)
    Z = B**(M - 1)
    W = ((Z - ONE)*B + BM1)/(B*Z)
    !
    Z = B**(EMAX - 2)
    DPMPAR = ((W * Z) * B) * B
    RETURN
END
!
SUBROUTINE CREC (X, Y, U, V)
    !-----------------------------------------------------------------------
    !             COMPLEX RECIPROCAL U + I*V = 1/(X + I*Y)
    !-----------------------------------------------------------------------
    IF (ABS(X) .GT. ABS(Y)) GO TO 10
    T = X/Y
    D = Y + T*X
    U = T/D
    V = -1.0/D
    RETURN
    10 T = Y/X
    D = X + T*Y
    U = 1.0/D
    V = -T/D
    RETURN
END
SUBROUTINE DCREC (X, Y, U, V)
    !-----------------------------------------------------------------------
    !             COMPLEX RECIPROCAL U + I*V = 1/(X + I*Y)
    !-----------------------------------------------------------------------
    DOUBLE PRECISION X, Y, U, V
    DOUBLE PRECISION D, T
    !
    IF (DABS(X) .GT. DABS(Y)) GO TO 10
    T = X/Y
    D = Y + T*X
    U = T/D
    V = -1.D0/D
    RETURN
    10 T = Y/X
    D = X + T*Y
    U = 1.D0/D
    V = -T/D
    RETURN
END
!
COMPLEX FUNCTION CDIV (A, B)
    !-----------------------------------------------------------------------
    !              COMPLEX DIVISION A/B WHERE B IS NONZERO
    !-----------------------------------------------------------------------
    COMPLEX A, B
    !
    AR = REAL(A)
    AI = AIMAG(A)
    BR = REAL(B)
    BI = AIMAG(B)
    !
    IF (ABS(BR) .LT. ABS(BI)) GO TO 10
    T = BI/BR
    D = BR + T*BI
    U = (AR + AI*T)/D
    V = (AI - AR*T)/D
    CDIV = CMPLX(U,V)
    RETURN
    10 T = BR/BI
    D = BI + T*BR
    U = (AR*T + AI)/D
    V = (AI*T - AR)/D
    CDIV = CMPLX(U,V)
    RETURN
END
!
SUBROUTINE SNHCSH (SINHM,COSHM,X,ISW)
    !
    INTEGER ISW
    REAL SINHM,COSHM,X,CUT(5)
    !
    !                      FROM THE SPLINE UNDER TENSION PACKAGE
    !                       CODED BY A. K. CLINE AND R. J. RENKA
    !                            DEPARTMENT OF COMPUTER SCIENCES
    !                              UNIVERSITY OF TEXAS AT AUSTIN
    !                          MODIFIED BY A.H. MORRIS (NSWC/DL)
    !
    ! THIS SUBROUTINE RETURNS APPROXIMATIONS TO
    !       SINHM(X) = SINH(X)-X
    !       COSHM(X) = COSH(X)-1
    ! AND
    !       COSHMM(X) = COSH(X)-1-X*X/2
    !
    ! ON INPUT--
    !
    !   X CONTAINS THE VALUE OF THE INDEPENDENT VARIABLE.
    !
    !   ISW INDICATES THE FUNCTION DESIRED
    !           = -1 IF ONLY SINHM IS DESIRED,
    !           =  0 IF BOTH SINHM AND COSHM ARE DESIRED,
    !           =  1 IF ONLY COSHM IS DESIRED,
    !           =  2 IF ONLY COSHMM IS DESIRED,
    !           =  3 IF BOTH SINHM AND COSHMM ARE DESIRED.
    !
    ! ON OUTPUT--
    !
    !   SINHM CONTAINS THE VALUE OF SINHM(X) IF ISW .LE. 0 OR
    !   ISW .EQ. 3 (SINHM IS UNALTERED IF ISW .EQ.1 OR ISW .EQ.2).
    !
    !   COSHM CONTAINS THE VALUE OF COSHM(X) IF ISW .EQ. 0 OR
    !   ISW .EQ. 1 AND CONTAINS THE VALUE OF COSHMM(X) IF ISW
    !   .GE. 2 (COSHM IS UNALTERED IF ISW .EQ. -1).
    !
    ! AND
    !
    !   X AND ISW ARE UNALTERED.
    !
    !-----------------------------------------------------------
    !
    DATA SP5/.255251817302048E-09/, SP4/.723809046696880E-07/, SP3/.109233297700241E-04/,SP2/.954811583154274E-03/
    DATA SP1/.452867078563929E-01/,SQ1/-.471329214363072E-02/
    DATA CP5/.116744361560051E-08/, CP4/.280407224259429E-06/,CP3/.344417983443219E-04/,CP2/.232293648552398E-02/
    DATA CP1/.778752378267155E-01/, CQ1/-.545809550662099E-02/
    DATA ZP3/5.59297116264720E-07/,ZP2/1.77943488030894E-04/,ZP1/1.69800461894792E-02/,ZQ4/1.33412535492375E-09/
    DATA ZQ3/-5.80858944138663E-07/, ZQ2/1.27814964403863E-04/, ZQ1/-1.63532871439181E-02/
    DATA CUT(1)/1.65/, CUT(2)/1.2/, CUT(3)/1.2/, CUT(4)/2.7/,CUT(5)/1.65/
    !
    XX = X
    AX = ABS(XX)
    XS = XX*XX
    IF (AX .GE. CUT(ISW+2)) EXPX = EXP(AX)
    !
    ! SINHM APPROXIMATION
    !
    IF (ISW .EQ. 1 .OR. ISW .EQ. 2) GO TO 2
    IF (AX .GE. 1.65) GO TO 1
    SINHM = ((((((SP5*XS+SP4)*XS+SP3)*XS+SP2)*XS+SP1)*XS+1.)*XS*XX)/((SQ1*XS+1.)*6.)
    GO TO 2
    1 SINHM = -(((AX+AX)+1./EXPX)-EXPX)/2.
    IF (XX .LT. 0.) SINHM = -SINHM
    !
    ! COSHM APPROXIMATION
    !
    2 IF (ISW .NE. 0 .AND. ISW .NE. 1) GO TO 4
    IF (AX .GE. 1.2) GO TO 3
    COSHM = ((((((CP5*XS+CP4)*XS+CP3)*XS+CP2)*XS+CP1)*XS+1.)*XS)/((CQ1*XS+1.)*2.)
    GO TO 4
    3 COSHM = ((1./EXPX-2.)+EXPX)/2.
    !
    ! COSHMM APPROXIMATION
    !
    4 IF (ISW .LE. 1) RETURN
    IF (AX .GE. 2.70) GO TO 5
    COSHM = ((((ZP3*XS+ZP2)*XS+ZP1)*XS+1.)*XS*XS)/(((((ZQ4*XS+ZQ3)*XS+ZQ2)*XS+ZQ1)*XS+1.)*24.)
    RETURN
    5 COSHM = (((1./EXPX-2.)-XS)+EXPX)/2.
    RETURN
END
!
REAL FUNCTION ALNREL(A)
    !-----------------------------------------------------------------------
    !            EVALUATION OF THE FUNCTION LN(1 + A)
    !-----------------------------------------------------------------------
    DATA P1/-.129418923021993E+01/, P2/.405303492862024E+00/,P3/-.178874546012214E-01/
    DATA Q1/-.162752256355323E+01/, Q2/.747811014037616E+00/, Q3/-.845104217945565E-01/
    !--------------------------
    IF (ABS(A) .GT. 0.375) GO TO 10
    T = A/(A + 2.0)
    T2 = T*T
    W = (((P3*T2 + P2)*T2 + P1)*T2 + 1.0)/(((Q3*T2 + Q2)*T2 + Q1)*T2 + 1.0)
    ALNREL = 2.0*T*W
    RETURN
    !
    10 X = 1.0 + A
    IF (A .LT. 0.0) X = (A + 0.5) + 0.5
    ALNREL = ALOG(X)
    RETURN
END
!
REAL FUNCTION REXP (X)
    !-----------------------------------------------------------------------
    !            EVALUATION OF THE FUNCTION EXP(X) - 1
    !-----------------------------------------------------------------------
    DATA P1/ .914041914819518E-09/,&
            P2/ .238082361044469E-01/,&
            Q1/-.499999999085958E+00/,&
            Q2/ .107141568980644E+00/,&
            Q3/-.119041179760821E-01/,&
            Q4/ .595130811860248E-03/
    !-----------------------
    IF (ABS(X) .GT. 0.15) GO TO 10
    REXP = X*(((P2*X + P1)*X + 1.0)/((((Q4*X + Q3)*X + Q2)*X+ Q1)*X + 1.0))
    RETURN
    !
    10 IF (X .LT. 0.0) GO TO 20
    E = EXP(X)
    REXP = E*(0.5 + (0.5 - 1.0/E))
    RETURN
    20 IF (X .LT. -37.0) GO TO 30
    REXP = (EXP(X) - 0.5) - 0.5
    RETURN
    30 REXP = -1.0
    RETURN
END
!
REAL FUNCTION COS1 (X)
    !-----------------------------------------------------------------------
    !                   EVALUATION  OF COS(X*PI)
    !-----------------------------------------------------------------------
    DATA A0 /.314159265358979E+01/, A1 /-.516771278004995E+01/
    DATA A2 /.255016403987327E+01/, A3 /-.599264528932149E+00/
    DATA A4 /.821458689493251E-01/, A5 /-.737001831310553E-02/
    DATA A6 /.461514425296398E-03/
    DATA B1 /-.493480220054460E+01/, B2 /.405871212639605E+01/
    DATA B3 /-.133526276691575E+01/, B4 /.235330543508553E+00/
    DATA B5 /-.258048861575714E-01/, B6 /.190653140279462E-02/
    !------------------------
    !
    !     ****** MAX IS A MACHINE DEPENDENT CONSTANT. MAX IS THE
    !            LARGEST POSITIVE INTEGER THAT MAY BE USED.
    !
    MAX = IPMPAR(3)
    !
    !------------------------
    A = ABS(X)
    IF (A .LT. FLOAT(MAX)) GO TO 10
    COS1 = 1.0
    RETURN
    !
    10 N = A
    A = A - FLOAT(N)
    IF (A .GT. 0.75) GO TO 20
    IF (A .LT. 0.25) GO TO 21
    !
    !                    0.25 .LE. A .LE. 0.75
    !
    A = 0.25 + (0.25 - A)
    T = A*A
    COS1 = ((((((A6*T + A5)*T + A4)*T + A3)*T + A2)*T+ A1)*T + A0)*A
    GO TO 30
    !
    !                 A .LT. 0.25  OR  A .GT. 0.75
    !
    20 A = 0.25 + (0.75 - A)
    N = N - 1
    21 T = A*A
    COS1 = ((((((B6*T + B5)*T + B4)*T + B3)*T + B2)*T+ B1)*T + 0.5) + 0.5
    !
    !                        TERMINATION
    !
    30 IF (MOD(N,2) .NE. 0) COS1 = - COS1
    RETURN
END
!
REAL FUNCTION SIN1 (X)
!-----------------------------------------------------------------------
!                   EVALUATION  OF SIN(X*PI)
!-----------------------------------------------------------------------
DATA A0 /.314159265358979E+01/, A1 /-.516771278004995E+01/
DATA A2 /.255016403987327E+01/, A3 /-.599264528932149E+00/
DATA A4 /.821458689493251E-01/, A5 /-.737001831310553E-02/
DATA A6 /.461514425296398E-03/
DATA B1 /-.493480220054460E+01/, B2 /.405871212639605E+01/
DATA B3 /-.133526276691575E+01/, B4 /.235330543508553E+00/
DATA B5 /-.258048861575714E-01/, B6 /.190653140279462E-02/
!------------------------
!
!     ****** MAX IS A MACHINE DEPENDENT CONSTANT. MAX IS THE
!            LARGEST POSITIVE INTEGER THAT MAY BE USED.
!
MAX = IPMPAR(3)
!
!------------------------
A = ABS(X)
IF (A .LT. FLOAT(MAX)) GO TO 10
SIN1 = 0.0
RETURN
!
10 N = A
A = A - FLOAT(N)
IF (A .GT. 0.75) GO TO 20
IF (A .LT. 0.25) GO TO 21
!
!                    0.25 .LE. A .LE. 0.75
!
A = 0.25 + (0.25 - A)
T = A*A
SIN1 = ((((((B6*T + B5)*T + B4)*T + B3)*T + B2)*T + B1)*T + 0.5) + 0.5
GO TO 30
!
!                 A .LT. 0.25  OR  A .GT. 0.75
!
20 A = 0.25 + (0.75 - A)
21 T = A*A
SIN1 = ((((((A6*T + A5)*T + A4)*T + A3)*T + A2)*T + A1)*T + A0)*A
!
!                        TERMINATION
!
30 IF (X .LT. 0.0) SIN1 = - SIN1
IF (MOD(N,2) .NE. 0) SIN1 = - SIN1
RETURN
END
!
COMPLEX FUNCTION CGAM0 (Z)
    !-----------------------------------------------------------------------
    !          EVALUATION OF 1/GAMMA(1 + Z)  FOR ABS(Z) .LT. 1.0
    !-----------------------------------------------------------------------
    COMPLEX Z, W
    REAL A(25)
    !-----------------------
    DATA A(1)  / .577215664901533E+00/, A(2)  /-.655878071520254E+00/
    DATA A(3)  /-.420026350340952E-01/, A(4)  / .166538611382291E+00/
    DATA A(5)  /-.421977345555443E-01/, A(6)  /-.962197152787697E-02/
    DATA A(7)  / .721894324666310E-02/, A(8)  /-.116516759185907E-02/
    DATA A(9)  /-.215241674114951E-03/, A(10) / .128050282388116E-03/
    DATA A(11) /-.201348547807882E-04/, A(12) /-.125049348214267E-05/
    DATA A(13) / .113302723198170E-05/, A(14) /-.205633841697761E-06/
    DATA A(15) / .611609510448142E-08/, A(16) / .500200764446922E-08/
    DATA A(17) /-.118127457048702E-08/, A(18) / .104342671169110E-09/
    DATA A(19) / .778226343990507E-11/, A(20) /-.369680561864221E-11/
    DATA A(21) / .510037028745448E-12/, A(22) /-.205832605356651E-13/
    DATA A(23) /-.534812253942302E-14/, A(24) / .122677862823826E-14/
    DATA A(25) /-.118125930169746E-15/
    !-----------------------
    N = 25
    X = REAL(Z)
    Y = AIMAG(Z)
    IF (X*X + Y*Y .LE. 0.04) N = 14
    !
    K = N
    W = A(N)
    DO 10 I = 2,N
        K = K - 1
        W = A(K) + Z*W
    10 CONTINUE
    CGAM0 = 1.0 + Z*W
    RETURN
END
!
    SUBROUTINE CGAMMA(MO,Z,W)
    !-----------------------------------------------------------------------
    !
    !        EVALUATION OF THE COMPLEX GAMMA AND LOGGAMMA FUNCTIONS
    !
    !                        ---------------
    !
    !     MO IS AN INTEGER, Z A COMPLEX ARGUMENT, AND W A COMPLEX VARIABLE.
    !
    !                 W = GAMMA(Z)       IF MO = 0
    !                 W = LN(GAMMA(Z))   OTHERWISE
    !
    !-----------------------------------------------------------------------
    INTEGER*4 MO
    COMPLEX Z,W
    COMPLEX ETA, ETA2, SUM
    REAL C0(12)
    !---------------------------
    !     ALPI = LOG(PI)
    !     HL2P = 0.5 * LOG(2*PI)
    !---------------------------
    DATA PI  /3.14159265358979/
    DATA PI2 /6.28318530717959/
    DATA ALPI/1.14472988584940/
    DATA HL2P/.918938533204673/
    !---------------------------
    DATA C0(1) /.833333333333333E-01/, C0(2) /-.277777777777778E-02/, &
            C0(3) /.793650793650794E-03/, C0(4) /-.595238095238095E-03/, &
            C0(5) /.841750841750842E-03/, C0(6) /-.191752691752692E-02/, &
            C0(7) /.641025641025641E-02/, C0(8) /-.295506535947712E-01/, &
            C0(9) /.179644372368831E+00/, C0(10)/-.139243221690590E+01/, &
            C0(11)/.134028640441684E+02/, C0(12)/-.156848284626002E+03/
    !---------------------------
    !
    !     ****** MAX AND EPS ARE MACHINE DEPENDENT CONSTANTS.
    !            MAX IS THE LARGEST POSITIVE INTEGER THAT MAY
    !            BE USED, AND EPS IS THE SMALLEST REAL NUMBER
    !            SUCH THAT 1.0 + EPS .GT. 1.0.
    !
    !MO = 0
    MAX = IPMPAR(3)
    EPS = SPMPAR(1)
    !
    !---------------------------
    X = REAL(Z)
    Y = AIMAG(Z)
    IF (X .GE. 0.0) GO TO 50
    !-----------------------------------------------------------------------
    !            CASE WHEN THE REAL PART OF Z IS NEGATIVE
    !-----------------------------------------------------------------------
    Y = ABS(Y)
    T = -PI * Y
    ET = EXP(T)
    E2T = ET * ET
    !
    !     SET  A1 = (1 + E2T)/2  AND  A2 = (1 - E2T)/2
    !
    A1 = 0.5 * (1.0 + E2T)
    T2 = T + T
    IF (T2 .LT. -0.15) GO TO 10
    A2 = -0.5 * REXP(T2)
    GO TO 20
    10 A2 = 0.5 * (0.5 + (0.5 - E2T))
    !
    !     COMPUTE SIN(PI*X) AND COS(PI*X)
    !
    20 IF (ABS(X) .GE. AMIN1(FLOAT(MAX), 1.0 / EPS)) GO TO 200
    K = ABS(X)
    U = X + K
    K = MOD(K, 2)
    IF (U .GT. -0.5) GO TO 21
    U = 0.5 + (0.5 + U)
    K = K + 1
    21 U = PI * U
    SN = SIN(U)
    CN = COS(U)
    IF (K .NE. 1) GO TO 30
    SN = -SN
    CN = -CN
    !
    !     SET  H1 + H2*I  TO  PI/SIN(PI*Z)  OR  LOG(PI/SIN(PI*Z))
    !
    30 A1 = SN * A1
    A2 = CN * A2
    A = A1 * A1 + A2 * A2
    IF (A .EQ. 0.0) GO TO 200
    IF (MO .NE. 0) GO TO 40
    !
    H1 = A1 / A
    H2 = -A2 / A
    C = PI * ET
    H1 = C * H1
    H2 = C * H2
    GO TO 41
    !
    40 H1 = (ALPI + T) - 0.5 * ALOG(A)
    H2 = -ATAN2(A2, A1)
    41 IF (AIMAG(Z) .LT. 0.0) GO TO 42
    X = 1.0 - X
    Y = -Y
    GO TO 50
    42 H2 = -H2
    X = 1.0 - X
    !-----------------------------------------------------------------------
    !           CASE WHEN THE REAL PART OF Z IS NONNEGATIVE
    !-----------------------------------------------------------------------
    50 W1 = 0.0
    W2 = 0.0
    N = 0
    T = X
    Y2 = Y * Y
    A = T * T + Y2
    CUT = 36.0
    IF (EPS .GT. 1.E-8) CUT = 16.0
    IF (A .GE. CUT) GO TO 80
    IF (A .EQ. 0.0) GO TO 200
    51    N = N + 1
    T = T + 1.0
    A = T * T + Y2
    IF (A .LT. CUT) GO TO 51
    !
    !     LET S1 + S2*I BE THE PRODUCT OF THE TERMS (Z+J)/(Z+N)
    !
    U1 = (X * T + Y2) / A
    U2 = Y / A
    S1 = U1
    S2 = N * U2
    IF (N .LT. 2) GO TO 70
    U = T / A
    NM1 = N - 1
    DO 60 J = 1, NM1
        V1 = U1 + J * U
        V2 = (N - J) * U2
        C = S1 * V1 - S2 * V2
        D = S1 * V2 + S2 * V1
        S1 = C
        S2 = D
    60 CONTINUE
    !
    !     SET  W1 + W2*I = LOG(S1 + S2*I)  WHEN MO IS NONZERO
    !
    70 S = S1 * S1 + S2 * S2
    IF (MO .EQ. 0) GO TO 80
    W1 = 0.5 * ALOG(S)
    W2 = ATAN2(S2, S1)
    !
    !     SET  V1 + V2*I = (Z - 0.5) * LOG(Z + N) - Z
    !
    80 T1 = 0.5 * ALOG(A) - 1.0
    T2 = ATAN2(Y, T)
    U = X - 0.5
    V1 = (U * T1 - 0.5) - Y * T2
    V2 = U * T2 + Y * T1
    !
    !     LET A1 + A2*I BE THE ASYMPTOTIC SUM
    !
    ETA = CMPLX(T / A, -Y / A)
    ETA2 = ETA * ETA
    M = 12
    IF (A .GE. 289.0) M = 6
    IF (EPS .GT. 1.E-8) M = M / 2
    SUM = CMPLX(C0(M), 0.0)
    L = M
    DO 90 J = 2, M
        L = L - 1
        SUM = CMPLX(C0(L), 0.0) + SUM * ETA2
    90 CONTINUE
    SUM = SUM * ETA
    A1 = REAL(SUM)
    A2 = AIMAG(SUM)
    !-----------------------------------------------------------------------
    !                 GATHERING TOGETHER THE RESULTS
    !-----------------------------------------------------------------------
    W1 = (((A1 + HL2P) - W1) + V1) - N
    W2 = (A2 - W2) + V2
    IF (REAL(Z) .LT. 0.0) GO TO 120
    IF (MO .NE. 0) GO TO 110
    !
    !     CASE WHEN THE REAL PART OF Z IS NONNEGATIVE AND MO = 0
    !
    A = EXP(W1)
    W1 = A * COS(W2)
    W2 = A * SIN(W2)
    IF (N .EQ. 0) GO TO 140
    C = (S1 * W1 + S2 * W2) / S
    D = (S1 * W2 - S2 * W1) / S
    W1 = C
    W2 = D
    GO TO 140
    !
    !     CASE WHEN THE REAL PART OF Z IS NONNEGATIVE AND MO IS NONZERO.
    !     THE ANGLE W2 IS REDUCED TO THE INTERVAL -PI .LT. W2 .LE. PI.
    !
    110 IF (W2 .GT. PI) GO TO 111
    K = 0.5 - W2 / PI2
    W2 = W2 + PI2 * K
    GO TO 140
    111 K = W2 / PI2 - 0.5
    W2 = W2 - PI2 * FLOAT(K + 1)
    IF (W2 .LE. -PI) W2 = PI
    GO TO 140
    !
    !     CASE WHEN THE REAL PART OF Z IS NEGATIVE AND MO IS NONZERO
    !
    120 IF (MO .EQ. 0) GO TO 130
    W1 = H1 - W1
    W2 = H2 - W2
    GO TO 110
    !
    !     CASE WHEN THE REAL PART OF Z IS NEGATIVE AND MO = 0
    !
    130 A = EXP(-W1)
    T1 = A * COS(-W2)
    T2 = A * SIN(-W2)
    W1 = H1 * T1 - H2 * T2
    W2 = H1 * T2 + H2 * T1
    IF (N .EQ. 0) GO TO 140
    C = W1 * S1 - W2 * S2
    D = W1 * S2 + W2 * S1
    W1 = C
    W2 = D
    !
    !     TERMINATION
    !
    140 W = CMPLX(W1, W2)
    RETURN
    !-----------------------------------------------------------------------
    !             THE REQUESTED VALUE CANNOT BE COMPUTED
    !-----------------------------------------------------------------------
    200 W = (0.0, 0.0)
    RETURN
END
!
SUBROUTINE CBSSLJ (Z,CNU,W)
    !-----------------------------------------------------------------------
    !
    !        EVALUATION OF THE COMPLEX BESSEL FUNCTION J   (Z)
    !                                                   CNU
    !-----------------------------------------------------------------------
    !
    !    WRITTEN BY
    !        ANDREW H. VAN TUYL AND ALFRED H. MORRIS, JR.
    !        NAVAL SURFACE WARFARE CENTER
    !        OCTOBER, 1991
    !
    !    A MODIFICATION OF THE PROCEDURE DEVELOPED BY ALLEN V. HERSHEY
    !    (NAVAL SURFACE WARFARE CENTER) IN 1978 FOR HANDLING THE DEBYE
    !    APPROXIMATION IS EMPLOYED.
    !
    !-----------------------------------------------------------------------
    COMPLEX Z, CNU, W
    COMPLEX C, NU, S, SM1, SM2, T, TSC, W0, W1, ZN, ZZ
    COMPLEX CDIV, CGAM0
    !-----------------------
    DATA PI /3.14159265358979/
    !-----------------------
    X = REAL(Z)
    Y = AIMAG(Z)
    R = CPABS(X,Y)
    CN1 = REAL(CNU)
    CN2 = AIMAG(CNU)
    RN2 = CN1*CN1 + CN2*CN2
    PN = AINT(CN1)
    FN = CN1 - PN
    !
    !         CALCULATION WHEN ORDER IS AN INTEGER
    !
    SN = 1.0
    IF (FN .NE. 0.0 .OR. CN2 .NE. 0.0) GO TO 10
    N = PN
    PN = ABS(PN)
    CN1 = PN
    IF (N .LT. 0 .AND. N .NE. (N/2)*2) SN = -1.0
    !
    !         SELECTION OF METHOD
    !
    10 IF (R .LE. 17.5) GO TO 20
    IF (R .GT. 17.5 + 0.5*RN2) GO TO 40
    GO TO 50
    !
    !         USE MACLAURIN EXPANSION AND RECURSION
    !
    20 IF (CN1 .GE. 0.0) GO TO 30
    QN = -1.25*(R + 0.5*ABS(CN2) - ABS(Y - 0.5*CN2))
    IF (CN1 .GE. QN) GO TO 30
    QN = 1.25*(R - AMAX1(1.2*R,ABS(Y - CN2)))
    IF (CN1 .GE. QN) GO TO 30
    QN = AMIN1(PN, -AINT(1.25*(R - ABS(CN2))))
    GO TO 130
    !
    30 R2 = R*R
    QM = 0.0625*R2*R2 - CN2*CN2
    QN = AMAX1(PN, AINT(SQRT(0.5*(QM + ABS(QM)))))
    GO TO 130
    !
    !         USE ASYMPTOTIC EXPANSION
    !
    40 CALL CBJA(Z, CNU, W)
    RETURN
    !
    !          CALCULATION FOR 17.5 .LT. ABS(Z) .LE. 17.5 + 0.5*ABS(CNU)**2
    !
    50 N = 0
    IF (ABS(CN2) .GE. 0.8*ABS(Y)) GO TO 60
    QM = -1.25*(R + 0.5*ABS(CN2) - ABS(Y - 0.5*CN2))
    IF (CN1 .GE. QM) GO TO 60
    QM = 1.25*(R - AMAX1(1.2*R,ABS(Y - CN2)))
    IF (CN1 .LT. QM) N = 1
    !
    60 QN = PN
    A = 4.E-3*R*R
    ZZ = Z
    IF (X .LT. 0.0) ZZ = -Z
    !
    !          CALCULATION OF ZONE OF EXCLUSION OF DEBYE APPROXIMATION
    !
    70    NU = CMPLX(QN + FN, CN2)
    ZN = NU/Z
    T2 = AIMAG(ZN)*AIMAG(ZN)
    U = 1.0 - REAL(ZN)
    T1 = U*U + T2
    U = 1.0 + REAL(ZN)
    T2 = U*U + T2
    U = T1*T2
    V = A*U/(T1*T1 + T2*T2)
    IF (U*V*V .GT. 1.0) GO TO 80
    !
    !          THE ARGUMENT LIES INSIDE THE ZONE OF EXCLUSION
    !
    QN = QN + 1.0
    IF (N .EQ. 0) GO TO 70
    !
    !          USE MACLAURIN EXPANSION WITH FORWARD RECURRENCE
    !
    QN = AMIN1(PN, -AINT(1.25*(R - ABS(CN2))))
    GO TO 130
    !
    !          USE BACKWARD RECURRENCE STARTING FROM THE
    !          ASYMPTOTIC EXPANSION
    !
    80 QNP1 = QN + 1.0
    IF (ABS(QN) .GE. ABS(PN)) GO TO 100
    IF (R .LT. 17.5 + 0.5*(QNP1*QNP1 + CN2*CN2)) GO TO 100
    !
    NU = CMPLX(QN + FN, CN2)
    CALL CBJA (ZZ, NU, SM1)
    NU = CMPLX(QNP1 + FN, CN2)
    CALL CBJA (ZZ, NU, SM2)
    GO TO 110
    !
    !          USE BACKWARD RECURRENCE STARTING FROM THE
    !           DEBYE APPROXIMATION
    !
    100 NU = CMPLX(QN + FN, CN2)
    CALL CBDB (ZZ, NU, FN, SM1)
    IF (QN .EQ. PN) GO TO 120
    NU = CMPLX(QNP1 + FN, CN2)
    CALL CBDB (ZZ, NU, FN, SM2)
    !
    110    NU = CMPLX(QN + FN, CN2)
    TSC = 2.0*NU*SM1/ZZ - SM2
    SM2 = SM1
    SM1 = TSC
    QN = QN - 1.0
    IF (QN .NE. PN) GO TO 110
    !
    120 W = SM1
    IF (SN .LT. 0.0) W = -W
    IF (X .GE. 0.0) RETURN
    !
    NU = PI*CMPLX(-CN2, CN1)
    IF (Y .LT. 0.0) NU = -NU
    W = CEXP(NU)*W
    RETURN
    !
    !          USE MACLAURIN EXPANSION WITH FORWARD OR BACKWARD RECURRENCE.
    !
    130 M = QN - PN
    IF (IABS(M) .GT. 1) GO TO 140
    NU = CMPLX(CN1, CN2)
    CALL CBJM (Z, NU, W)
    GO TO 180
    140 NU = CMPLX(QN + FN, CN2)
    CALL CBJM (Z, NU, W1)
    W0 = 0.25*Z*Z
    IF (M .GT. 0) GO TO 160
    !
    !          FORWARD RECURRENCE
    !
    M = IABS(M)
    NU = NU + 1.0
    CALL CBJM (Z, NU, W)
    DO 150 I = 2,M
        C = NU*(NU + 1.0)
        T = (C/W0)*(W - W1)
        W1 = W
        W = T
        NU = NU + 1.0
    150 CONTINUE
    GO TO 180
    !
    !          BACKWARD RECURRENCE
    !
    160 NU = NU - 1.0
    CALL CBJM (Z, NU, W)
    DO 170 I = 2,M
        C = NU*(NU + 1.0)
        T = (W0/C)*W1
        W1 = W
        W = W - T
        NU = NU - 1.0
    170 CONTINUE
    !
    !          FINAL ASSEMBLY
    !
    180 IF (FN .NE. 0.0 .OR. CN2 .NE. 0.0) GO TO 190
    K = PN
    IF (K .EQ. 0.0) RETURN
    E = SN/GAMMA(PN + 1.0)
    W = E*W*(0.5*Z)**K
    RETURN
    !
    190 S = CNU*CLOG(0.5*Z)
    W = CEXP(S)*W
    IF (RN2 .GT. 0.81) GO TO 200
    W = W*CGAM0(CNU)
    RETURN
    200 CALL CGAMMA(0, CNU, T)
    W = CDIV(W, CNU*T)
    RETURN
END
!
SUBROUTINE CBJA (CZ, CNU, W)
    !-----------------------------------------------------------------------
    !        COMPUTATION OF J(NU,Z) BY THE ASYMPTOTIC EXPANSION
    !-----------------------------------------------------------------------
    COMPLEX CZ, CNU, W
    REAL INU, M
    COMPLEX A, A1, ARG, E, ETA, J, NU, P, Q, T, Z, ZR, ZZ
    !--------------------------
    !     PIHALF = PI/2
    !     C = 2*PI**(-1/2)
    !--------------------------
    DATA PIHALF /1.5707963267949/
    DATA C /1.12837916709551/
    DATA J /(0.0, 1.0)/
    !--------------------------
    ANORM(Z) = AMAX1(ABS(REAL(Z)), ABS(AIMAG(Z)))
    !
    !--------------------------
    !
    !     ****** EPS IS A MACHINE DEPENDENT CONSTANT. EPS IS THE
    !            SMALLEST NUMBER SUCH THAT 1.0 + EPS .GT. 1.0 .
    !
    EPS = SPMPAR(1)
    !
    !--------------------------
    Z = CZ
    X = REAL(Z)
    Y = AIMAG(Z)
    NU = CNU
    IND = 0
    IF (ABS(X) .GT. 1.E-2*ABS(Y)) GO TO 10
    IF (AIMAG(NU) .GE. 0.0 .OR. ABS(REAL(NU)) .GE.1.E-2*ABS(AIMAG(NU))) GO TO 10
        IND = 1
        NU = CONJG(NU)
        Z = CONJG(Z)
        Y = -Y
        !
        10 IF (X .LT. -1.E-2*Y) Z = -Z
        ZZ = Z + Z
        CALL CREC (REAL(ZZ), AIMAG(ZZ), U, V)
        ZR = CMPLX(U, V)
        ETA = -ZR*ZR
        !
        P = (0.0,0.0)
        Q = (0.0,0.0)
        A1 = NU*NU - 0.25
        A = A1
        T = A1
        M = 1.0
        TOL = EPS*ANORM(A1)
        DO 20 I = 1,16
            A = A - 2.0*M
            M = M + 1.0
            T = T*A*ETA/M
            P = P + T
            A = A - 2.0*M
            M = M + 1.0
            T = T*A/M
            Q = Q + T
            IF (ANORM(T) .LE. TOL) GO TO 30
        20 CONTINUE
        !
        30 P = P + 1.0
        Q = (Q + A1)*ZR
        W = Z - PIHALF*NU
        IF (ABS(AIMAG(W)) .GT. 1.0) GO TO 40
        ARG = W - 0.5*PIHALF
        W = C*CSQRT(ZR)*(P*CCOS(ARG) - Q*CSIN(ARG))
        GO TO 50
        40 E = CEXP(-J*W)
        T = Q - J*P
        IF (AIMAG(Z) .GT. 0.0 .AND. REAL(Z) .LE. 1.E-2*AIMAG(Z) .AND. ABS(REAL(NU)) .LT. 1.E-2*AIMAG(NU)) T = 0.5*T
            CALL CREC(REAL(E), AIMAG(E), U, V)
            W = 0.5*C*CSQRT(J*ZR)*((P - J*Q)*E + T*CMPLX(U, V))
            !
            50 IF (X .GE. -1.E-2*Y) GO TO 60
            IF (Y .LT. 0.0) NU = -NU
            !
            !     COMPUTATION OF EXP(I*PI*NU)
            !
            RNU = REAL(NU)
            INU = AIMAG(NU)
            R = EXP(-2.0*PIHALF*INU)
            U = R*COS1(RNU)
            V = R*SIN1(RNU)
            W = W*CMPLX(U,V)
            !
            60 IF (IND .NE. 0) W = CONJG(W)
            RETURN
END
!
SUBROUTINE CBJM (Z, CNU, W)
    !-----------------------------------------------------------------------
    !
    !       COMPUTATION OF  (Z/2)**(-CNU) * GAMMA(CNU + 1) * J(CNU,Z)
    !
    !                           -----------------
    !
    !     THE MACLAURIN EXPANSION IS USED. IT IS ASSUMED THAT CNU IS NOT
    !     A NEGATIVE INTEGER.
    !
    !-----------------------------------------------------------------------
    COMPLEX CNU, NU, NUP1, P, S, SN, T, TI, W, Z
    REAL INU, M
    COMPLEX CDIV
    !--------------------------
    ANORM(Z) = AMAX1(ABS(REAL(Z)),ABS(AIMAG(Z)))
    !--------------------------
    !
    !     ****** EPS IS A MACHINE DEPENDENT CONSTANT. EPS IS THE
    !            SMALLEST NUMBER SUCH THAT 1.0 + EPS .GT. 1.0 .
    !
    EPS = SPMPAR(1)
    !
    !--------------------------
    S = -0.25*(Z*Z)
    NU = CNU
    RNU = REAL(NU)
    INU = AIMAG(NU)
    A = 0.5 + (0.5 + RNU)
    NUP1 = CMPLX(A, INU)
    !
    IF (A .LE. 0.0) GO TO 10
    M = 1.0
    T = S/NUP1
    W = 1.0 + T
    GO TO 70
    !
    !     ADD 1.0 AND THE FIRST K-1 TERMS
    !
    10 K = INT(-A) + 2
    KM1 = K - 1
    W = (1.0, 0.0)
    T = W
    DO 20 I = 1,KM1
        M = I
        T = T*(S/(M*(NU + M)))
        W = W + T
        IF (ANORM(T) .LE. EPS*ANORM(W)) GO TO 30
    20 CONTINUE
    GO TO 70
    !
    !     CHECK IF THE (K-1)-ST AND K-TH TERMS CAN BE IGNORED.
    !     IF SO THEN THE SUMMATION IS COMPLETE.
    !
    30 IF (I .EQ. KM1) GO TO 70
    IMIN = I + 1
    IF (IMIN .GE. K - 5) GO TO 50
    TI = T
    !
    M = KM1
    T = S/(NU + M)
    A0 = ANORM(T)/M
    T = T * (S/(NU + (M + 1.0)))
    A = ANORM(T)/(M*(M + 1.0))
    A = AMAX1(A, A0)
    !
    T = (1.0, 0.0)
    KM2 = K - 2
    DO 40 I = IMIN,KM2
        M = I
        T = T*(S/(M*(NU + M)))
        IF (A*ANORM(T) .LT. 0.5) RETURN
    40 CONTINUE
    T = T*TI
    IMIN = KM2
    !
    !     ADD THE (K-1)-ST TERM
    !
    50 A = 1.0
    P = (1.0, 0.0)
    SN = P
    DO 60 I = IMIN,KM1
        M = I
        A = A*M
        P = P*(NU + M)
        SN = S*SN
    60 CONTINUE
    T = T*(CDIV(SN,P)/A)
    W = W + T
    !
    !     ADD THE REMAINING TERMS
    !
    70 M = M + 1.0
    T = T*(S/(M*(NU + M)))
    W = W + T
    IF (ANORM(T) .GT. EPS*ANORM(W)) GO TO 70
    !
    RETURN
END
!
SUBROUTINE CBDB (CZ, CNU, FN, W)
    !-----------------------------------------------------------------------
    !
    !         CALCULATION OF J   (CZ) BY THE DEBYE APPROXIMATION
    !                         CNU
    !                         ------------------
    !
    !     IT IS ASSUMED THAT REAL(CZ) .GE. 0 AND THAT REAL(CNU) = FN + K
    !     WHERE K IS AN INTEGER.
    !
    !-----------------------------------------------------------------------
    COMPLEX CZ, CNU, W
    REAL A(136), IS, INU, IZN
    COMPLEX C1, C2, ETA, J, NU, P, P1, Q, R, S, S1, S2, SM, T, Z, ZN
    !----------------------
    !     C = 1/SQRT(2)
    !     BND = PI/3
    !----------------------
    DATA J   /(0.0, 1.0)/
    DATA C   /.398942280401433/
    DATA PI  /3.14159265358979/
    DATA PI2 /6.28318530717959/
    DATA BND /1.04719755119660/
    !----------------------
    !
    !             COEFFICIENTS OF THE FIRST 16 POLYNOMIALS
    !                   IN THE DEBYE APPROXIMATION
    !
    !
    DATA A(1)  /1.0/
    DATA A(2)  /-.208333333333333E+00/, A(3)  / .125000000000000E+00/
    DATA A(4)  / .334201388888889E+00/, A(5)  /-.401041666666667E+00/, A(6)  / .703125000000000E-01/
    DATA A(7)  /-.102581259645062E+01/, A(8)  / .184646267361111E+01/, A(9)  /-.891210937500000E+00/
    DATA A(10) / .732421875000000E-01/
    DATA A(11) / .466958442342625E+01/, A(12) /-.112070026162230E+02/, A(13) / .878912353515625E+01/, A(14) /-.236408691406250E+01/
    DATA A(15) / .112152099609375E+00/
    DATA A(16) /-.282120725582002E+02/, A(17) / .846362176746007E+02/, A(18) /-.918182415432400E+02/, A(19) / .425349987453885E+02/
    DATA A(20) /-.736879435947963E+01/, A(21) / .227108001708984E+00/
    DATA A(22) / .212570130039217E+03/, A(23) /-.765252468141182E+03/, A(24) / .105999045252800E+04/, A(25) /-.699579627376133E+03/
    DATA A(26) / .218190511744212E+03/, A(27) /-.264914304869516E+02/,A(28) / .572501420974731E+00/
    DATA A(29) /-.191945766231841E+04/, A(30) / .806172218173731E+04/, A(31) /-.135865500064341E+05/, A(32) / .116553933368645E+05/
    DATA A(33) /-.530564697861340E+04/, A(34) / .120090291321635E+04/, A(35) /-.108090919788395E+03/, A(36) / .172772750258446E+01/
    DATA A(37) / .202042913309661E+05/, A(38) /-.969805983886375E+05/, A(39) / .192547001232532E+06/, A(40) /-.203400177280416E+06/
    DATA A(41) / .122200464983017E+06/, A(42) /-.411926549688976E+05/, A(43) / .710951430248936E+04/, A(44) /-.493915304773088E+03/
    DATA A(45) / .607404200127348E+01/
    DATA A(46) /-.242919187900551E+06/, A(47) / .131176361466298E+07/, A(48) /-.299801591853811E+07/, A(49) / .376327129765640E+07/
    DATA A(50) /-.281356322658653E+07/, A(51) / .126836527332162E+07/, A(52) /-.331645172484564E+06/, A(53) / .452187689813627E+05/
    DATA A(54) /-.249983048181121E+04/, A(55) / .243805296995561E+02/
    DATA A(56) / .328446985307204E+07/, A(57) /-.197068191184322E+08/, A(58) / .509526024926646E+08/, A(59) /-.741051482115327E+08/
    DATA A(60) / .663445122747290E+08/, A(61) /-.375671766607634E+08/, A(62) / .132887671664218E+08/, A(63) /-.278561812808645E+07/
    DATA A(64) / .308186404612662E+06/, A(65) /-.138860897537170E+05/, A(66) / .110017140269247E+03/
    DATA A(67) /-.493292536645100E+08/, A(68) / .325573074185766E+09/, A(69) /-.939462359681578E+09/, A(70) / .155359689957058E+10/
    DATA A(71) /-.162108055210834E+10/, A(72) / .110684281682301E+10/, A(73) /-.495889784275030E+09/, A(74) / .142062907797533E+09/
    DATA A(75) /-.244740627257387E+08/, A(76) / .224376817792245E+07/, A(77) /-.840054336030241E+05/, A(78) / .551335896122021E+03/
    DATA A(79) / .814789096118312E+09/, A(80) /-.586648149205185E+10/,  A(81) / .186882075092958E+11/, A(82) /-.346320433881588E+11/
    DATA A(83) / .412801855797540E+11/, A(84) /-.330265997498007E+11/, A(85) / .179542137311556E+11/, A(86) /-.656329379261928E+10/
    DATA A(87) / .155927986487926E+10/, A(88) /-.225105661889415E+09/, A(89) / .173951075539782E+08/, A(90) /-.549842327572289E+06/
    DATA A(91) / .303809051092238E+04/
    DATA A(92) /-.146792612476956E+11/, A(93) / .114498237732026E+12/, A(94) /-.399096175224466E+12/, A(95) / .819218669548577E+12/
    DATA A(96) /-.109837515608122E+13/, A(97) / .100815810686538E+13/, A(98) /-.645364869245377E+12/, A(99) / .287900649906151E+12/
    DATA A(100)/-.878670721780233E+11/, A(101)/ .176347306068350E+11/, A(102)/-.216716498322380E+10/, A(103)/ .143157876718889E+09/
    DATA A(104)/-.387183344257261E+07/, A(105)/ .182577554742932E+05/
    DATA A(106)/ .286464035717679E+12/, A(107)/-.240629790002850E+13/, A(108)/ .910934118523990E+13/, A(109)/-.205168994109344E+14/
    DATA A(110)/ .305651255199353E+14/, A(111)/-.316670885847852E+14/, A(112)/ .233483640445818E+14/, A(113)/-.123204913055983E+14/
    DATA A(114)/ .461272578084913E+13/, A(115)/-.119655288019618E+13/, A(116)/ .205914503232410E+12/, A(117)/-.218229277575292E+11/
    DATA A(118)/ .124700929351271E+10/, A(119)/-.291883881222208E+08/, A(120)/ .118838426256783E+06/
    DATA A(121)/-.601972341723401E+13/, A(122)/ .541775107551060E+14/, A(123)/-.221349638702525E+15/, A(124)/ .542739664987660E+15/
    DATA A(125)/-.889496939881026E+15/, A(126)/ .102695519608276E+16/, A(127)/-.857461032982895E+15/, A(128)/ .523054882578445E+15/
    DATA A(129)/-.232604831188940E+15/, A(130)/ .743731229086791E+14/, A(131)/-.166348247248925E+14/, A(132)/ .248500092803409E+13/
    DATA A(133)/-.229619372968246E+12/, A(134)/ .114657548994482E+11/,A(135)/-.234557963522252E+09/, A(136)/ .832859304016289E+06/
    !----------------------
    Z = CZ
    NU = CNU
    INU = AIMAG(CNU)
    IF (INU .GE. 0.0) GO TO 10
    Z = CONJG(Z)
    NU = CONJG(NU)
    10 X = REAL(Z)
    Y = AIMAG(Z)
    !
    !          TANH(GAMMA) = SQRT(1 - (Z/NU)**2) = W/NU
    !          T = EXP(NU*(TANH(GAMMA) - GAMMA))
    !
    ZN = Z/NU
    IZN = AIMAG(ZN)
    IF (ABS(IZN) .GT. 0.1*ABS(REAL(ZN))) GO TO 20
    !
    S = (1.0 - ZN)*(1.0 + ZN)
    ETA = 1.0/S
    Q = CSQRT(S)
    S = 1.0/(NU*Q)
    T = ZN/(1.0 + Q)
    T = CEXP(NU*(Q + CLOG(T)))
    GO TO 30
    !
    20 S = (NU - Z)*(NU + Z)
    ETA = (NU*NU)/S
    W = CSQRT(S)
    Q = W/NU
    IF (REAL(Q) .LT. 0.0) W = -W
    S = 1.0/W
    T = Z/(NU + W)
    T = CEXP(W + NU*CLOG(T))
    !
    30 IS = AIMAG(S)
    R = CSQRT(S)
    C1 = R*T
    AR = REAL(R)*REAL(R) + AIMAG(R)*AIMAG(R)
    AQ = -1.0/(REAL(Q)*REAL(Q) + AIMAG(Q)*AIMAG(Q))
    !
    PHI = ATAN2(Y,X)/3.0
    Q = NU - Z
    THETA = ATAN2(AIMAG(Q),REAL(Q)) - PHI
    IND = 0
    IF (ABS(THETA) .LT. 2.0*BND) GO TO 50
    !
    IND = 1
    CALL CREC(REAL(T), AIMAG(T), U, V)
    C2 = -J*R*CMPLX(U, V)
    IF (IS .LT. 0.0) GO TO 40
    IF (IS .GT. 0.0) GO TO 50
    IF (REAL(S) .LE. 0.0) GO TO 50
    40 C2 = -C2
    !
    !          SUMMATION OF THE SERIES S1 AND S2
    !
    50 SM = S*S
    P  = (A(2)*ETA + A(3))*S
    P1 = ((A(4)*ETA + A(5))*ETA + A(6))*SM
    S1 = (1.0 + P) + P1
    IF (IND .NE. 0) S2 = (1.0 - P) + P1
    SGN = 1.0
    AM = AR*AR
    M = 4
    L = 6
    !
    !          P = VALUE OF THE M-TH POLYNOMIAL
    !
    60 L = L + 1
    ALPHA = A(L)
    P = CMPLX(A(L),0.0)
    DO 70 K = 2,M
        L = L + 1
        ALPHA = A(L) + AQ*ALPHA
        P = A(L) + ETA*P
    70 CONTINUE
    !
    !          ONLY THE S1 SUM IS FORMED WHEN IND = 0
    !
    SM = S*SM
    P = P*SM
    S1 = S1 + P
    IF (IND .EQ. 0) GO TO 80
    SGN = -SGN
    S2 = S2 + SGN*P
    80 AM = AR*AM
    IF (1.0 + ALPHA*AM .EQ. 1.0) GO TO 100
    M = M + 1
    IF (M .LE. 16) GO TO 60
    !
    !          FINAL ASSEMBLY
    !
    100 S1 = C*C1*S1
    IF (IND .NE. 0) GO TO 110
    W = S1
    GO TO 200
    !
    110 S2 = C*C2*S2
    Q = NU + Z
    THETA = ATAN2(AIMAG(Q),REAL(Q)) - PHI
    IF (ABS(THETA) .GT. BND) GO TO 120
    W = S1 + S2
    GO TO 200
    !
    120 ALPHA = PI2
    IF (IZN .LT. 0.0) ALPHA = -ALPHA
    T = ALPHA*CMPLX(ABS(INU), -FN)
    ALPHA = EXP(REAL(T))
    U = AIMAG(T)
    R = CMPLX(COS(U),SIN(U))
    T = S1 - (ALPHA*R)*S1
    IF (X .EQ. 0.0 .AND. INU .EQ. 0.0) T = -T
    !
    IF (Y .GE. 0.0) GO TO 170
    IF (IZN .GE. 0.0 .AND. THETA .LE. SIGN(PI,THETA)) S2 = S2*(CONJG(R)/ALPHA)
        IF (X .EQ. 0.0) GO TO 180
        IF (IZN .LT. 0.0) GO TO 170
        IF (IS .LT. 0.0) GO TO 180
        !
        170 W = S2 + T
        GO TO 200
        180 W = S2 - T
        !
        200 IF (INU .LT. 0.0) W = CONJG(W)
        RETURN
END
!
SUBROUTINE BSSLJ (A, IN, W)
    !     ******************************************************************
    !     FORTRAN SUBROUTINE FOR ORDINARY BESSEL FUNCTION OF INTEGRAL ORDER
    !     ******************************************************************
    !     A  = ARGUMENT (COMPLEX NUMBER)
    !     IN = ORDER (INTEGER)
    !     W  = FUNCTION OF FIRST KIND (COMPLEX NUMBER)
    !     -------------------
    COMPLEX A, W
    DIMENSION AZ(2), FJ(2)
    DIMENSION CD(30), CE(30)
    DIMENSION QZ(2), RZ(2), SZ(2), ZR(2)
    DIMENSION TS(2), TM(2), RM(4), SM(4), AQ(2), QF(2)
    DATA CD(1) / 0.00000000000000E00/,  CD(2) /-1.64899505142212E-2/, CD(3) /-7.18621880068536E-2/,  CD(4) /-1.67086878124866E-1/
    DATA CD(5) /-3.02582250219469E-1/,  CD(6) /-4.80613945245927E-1/,CD(7) /-7.07075239357898E-1/,  CD(8) /-9.92995790539516E-1/
    DATA CD(9) /-1.35583925612592E00/,  CD(10)/-1.82105907899132E00/,CD(11)/-2.42482175310879E00/,  CD(12)/-3.21956655708750E00/
    DATA CD(13)/-4.28658077248384E00/,  CD(14)/-5.77022816798128E00/,CD(15)/-8.01371260952526E00/
    DATA CD(16)/ 0.00000000000000E00/,  CD(17)/-5.57742429879505E-3/, CD(18)/-4.99112944172476E-2/,  CD(19)/-1.37440911652397E-1/
    DATA CD(20)/-2.67233784710566E-1/,  CD(21)/-4.40380166808682E-1/, CD(22)/-6.61813614872541E-1/,  CD(23)/-9.41861077665017E-1/
    DATA CD(24)/-1.29754130468326E00/,  CD(25)/-1.75407696719816E00/, CD(26)/-2.34755299882276E00/,  CD(27)/-3.13041332689196E00/
    DATA CD(28)/-4.18397120563729E00/,  CD(29)/-5.65251799214994E00/, CD(30)/-7.87863959810677E00/
    DATA CE(1) / 0.00000000000000E00/,  CE(2) /-4.80942336387447E-3/, CE(3) /-1.31366200347759E-2/,  CE(4) /-1.94843834008458E-2/
    DATA CE(5) /-2.19948900032003E-2/,  CE(6) /-2.09396625676519E-2/, CE(7) /-1.74600268458650E-2/,  CE(8) /-1.27937813362085E-2/
    DATA CE(9) /-8.05234421796592E-3/,  CE(10)/-4.15817375002760E-3/, CE(11)/-1.64317738747922E-3/,  CE(12)/-4.49175585314709E-4/
    DATA CE(13)/-7.28594765574007E-5/,  CE(14)/-5.38265230658285E-6/, CE(15)/-9.93779048036289E-8/
    DATA CE(16)/ 0.00000000000000E00/,  CE(17)/ 7.53805779200591E-2/, CE(18)/ 7.12293537403464E-2/,  CE(19)/ 6.33116224228200E-2/
    DATA CE(20)/ 5.28240264523301E-2/,  CE(21)/ 4.13305359441492E-2/, CE(22)/ 3.01350573947510E-2/,  CE(23)/ 2.01043439592720E-2/
    DATA CE(24)/ 1.18552223068074E-2/,  CE(25)/ 5.86055510956010E-3/, CE(26)/ 2.25465148267325E-3/,  CE(27)/ 6.08173041536336E-4/
    DATA CE(28)/ 9.84215550625747E-5/,  CE(29)/ 7.32139093038089E-6/, CE(30)/ 1.37279667384666E-7/
    !     -------------------
    AZ(1)=REAL(A)
    AZ(2)=AIMAG(A)
    ZS=AZ(1)*AZ(1)+AZ(2)*AZ(2)
    ZM=SQRT(ZS)
    PN=IABS(IN)
    SN=+1.0
    IF(IN)002,003,003
    002 IF(IN.EQ.IN/2*2)GO TO 003
    SN=-1.0
    003 IF(AZ(1))004,005,005
    004 QZ(1)=-AZ(1)
    QZ(2)=-AZ(2)
    IF(IN.EQ.IN/2*2)GO TO 006
    SN=-SN
    GO TO 006
    005 QZ(1)=+AZ(1)
    QZ(2)=+AZ(2)
    006 IF(ZM.LE.17.5+0.5*PN*PN)GO TO 007
    QN=PN
    GO TO 013
    007 QN=0.5*ZM-0.5*ABS(QZ(2))+0.5*ABS(0.5*ZM-ABS(QZ(2)))
    IF(PN.LE.QN)GO TO 008
    QN=+AINT(0.0625*ZS)
    IF(PN.LE.QN)GO TO 031
    QN=PN
    GO TO 031
    008 IF(ZM.LE.17.5)GO TO 009
    QN=+AINT(SQRT(2.0*(ZM-17.5)))
    GO TO 013
    009 IF(ZS-1.0)011,010,010
    010 IF(-ABS(AZ(2))+0.096*AZ(1)*AZ(1))011,012,012
    011 QN=+AINT(0.0625*ZS)
    IF(PN.LE.QN)GO TO 031
    QN=PN
    GO TO 031
    012 QN=0.0
    013 SZ(1)=QZ(1)
    SZ(2)=QZ(2)
    QM=SN*0.797884560802865
    ZR(1)=SQRT(SZ(1)+ZM)
    ZR(2)=SZ(2)/ZR(1)
    ZR(1)=0.707106781186548*ZR(1)
    ZR(2)=0.707106781186548*ZR(2)
    QF(1)=+QM*ZR(1)/ZM
    QF(2)=-QM*ZR(2)/ZM
    IF(ZM.LE.17.5)GO TO 018
    014 RZ(1)=+0.5*QZ(1)/ZS
    RZ(2)=-0.5*QZ(2)/ZS
    AN=QN*QN-0.25
    SM(1)=0.0
    SM(2)=0.0
    SM(3)=0.0
    SM(4)=0.0
    TM(1)=1.0
    TM(2)=0.0
    PM=0.0
    GO TO 016
    015 AN=AN-2.0*PM
    PM=PM+1.0
    TS(1)=TM(1)*RZ(1)-TM(2)*RZ(2)
    TS(2)=TM(1)*RZ(2)+TM(2)*RZ(1)
    TM(1)=-AN*TS(1)/PM
    TM(2)=-AN*TS(2)/PM
    016 SM(1)=SM(1)+TM(1)
    SM(2)=SM(2)+TM(2)
    AN=AN-2.0*PM
    PM=PM+1.0
    TS(1)=TM(1)*RZ(1)-TM(2)*RZ(2)
    TS(2)=TM(1)*RZ(2)+TM(2)*RZ(1)
    TM(1)=+AN*TS(1)/PM
    TM(2)=+AN*TS(2)/PM
    IF(ABS(SM(3))+ABS(TM(1)).NE.ABS(SM(3)))GO TO 017
    IF(ABS(SM(4))+ABS(TM(2)).EQ.ABS(SM(4)))GO TO 020
    017 SM(3)=SM(3)+TM(1)
    SM(4)=SM(4)+TM(2)
    IF(PM.LT.35.0)GO TO 015
    GO TO 020
    018 SM(1)=1.0
    SM(2)=0.0
    SM(3)=1.0
    SM(4)=0.0
    M=15.0*QN+2.0
    N=15.0*QN+15.0
    DO 019 I=M,N
        TS(1)=+QZ(2)-CD(I)
        TS(2)=-QZ(1)
        SS=TS(1)*TS(1)+TS(2)*TS(2)
        TM(1)=+CE(I)*TS(1)/SS
        TM(2)=-CE(I)*TS(2)/SS
        SM(1)=SM(1)+TM(1)
        SM(2)=SM(2)+TM(2)
        TS(1)=-QZ(2)-CD(I)
        TS(2)=+QZ(1)
        SS=TS(1)*TS(1)+TS(2)*TS(2)
        TM(1)=+CE(I)*TS(1)/SS
        TM(2)=-CE(I)*TS(2)/SS
        SM(3)=SM(3)+TM(1)
        SM(4)=SM(4)+TM(2)
    019 CONTINUE
    TS(1)=+0.5*(SM(2)-SM(4))
    TS(2)=-0.5*(SM(1)-SM(3))
    SM(1)=+0.5*(SM(1)+SM(3))
    SM(2)=+0.5*(SM(2)+SM(4))
    SM(3)=TS(1)
    SM(4)=TS(2)
    020 AQ(1)=QZ(1)-1.57079632679490*(QN+0.5)
    AQ(2)=QZ(2)
    TS(1)=+COS(AQ(1))*0.5*(EXP(+AQ(2))+EXP(-AQ(2)))
    TS(2)=-SIN(AQ(1))*0.5*(EXP(+AQ(2))-EXP(-AQ(2)))
    TM(1)=SM(1)*TS(1)-SM(2)*TS(2)
    TM(2)=SM(1)*TS(2)+SM(2)*TS(1)
    TS(1)=+SIN(AQ(1))*0.5*(EXP(+AQ(2))+EXP(-AQ(2)))
    TS(2)=+COS(AQ(1))*0.5*(EXP(+AQ(2))-EXP(-AQ(2)))
    RM(1)=TM(1)-SM(3)*TS(1)+SM(4)*TS(2)
    RM(2)=TM(2)-SM(3)*TS(2)-SM(4)*TS(1)
    IF(QN.EQ.PN)GO TO 030
    RM(3)=RM(1)
    RM(4)=RM(2)
    QN=QN+1.0
    IF(ZM.LE.17.5)GO TO 025
    021 AN=QN*QN-0.25
    SM(1)=0.0
    SM(2)=0.0
    SM(3)=0.0
    SM(4)=0.0
    TM(1)=1.0
    TM(2)=0.0
    PM=0.0
    GO TO 023
    022 AN=AN-2.0*PM
    PM=PM+1.0
    TS(1)=TM(1)*RZ(1)-TM(2)*RZ(2)
    TS(2)=TM(1)*RZ(2)+TM(2)*RZ(1)
    TM(1)=-AN*TS(1)/PM
    TM(2)=-AN*TS(2)/PM
    023 SM(1)=SM(1)+TM(1)
    SM(2)=SM(2)+TM(2)
    AN=AN-2.0*PM
    PM=PM+1.0
    TS(1)=TM(1)*RZ(1)-TM(2)*RZ(2)
    TS(2)=TM(1)*RZ(2)+TM(2)*RZ(1)
    TM(1)=+AN*TS(1)/PM
    TM(2)=+AN*TS(2)/PM
    IF(ABS(SM(3))+ABS(TM(1)).NE.ABS(SM(3)))GO TO 024
    IF(ABS(SM(4))+ABS(TM(2)).EQ.ABS(SM(4)))GO TO 027
    024 SM(3)=SM(3)+TM(1)
    SM(4)=SM(4)+TM(2)
    IF(PM.LT.35.0)GO TO 022
    GO TO 027
    025 SM(1)=1.0
    SM(2)=0.0
    SM(3)=1.0
    SM(4)=0.0
    M=15.0*QN+2.0
    N=15.0*QN+15.0
    DO 026 I=M,N
        TS(1)=+QZ(2)-CD(I)
        TS(2)=-QZ(1)
        SS=TS(1)*TS(1)+TS(2)*TS(2)
        TM(1)=+CE(I)*TS(1)/SS
        TM(2)=-CE(I)*TS(2)/SS
        SM(1)=SM(1)+TM(1)
        SM(2)=SM(2)+TM(2)
        TS(1)=-QZ(2)-CD(I)
        TS(2)=+QZ(1)
        SS=TS(1)*TS(1)+TS(2)*TS(2)
        TM(1)=+CE(I)*TS(1)/SS
        TM(2)=-CE(I)*TS(2)/SS
        SM(3)=SM(3)+TM(1)
        SM(4)=SM(4)+TM(2)
    026 CONTINUE
    TS(1)=+0.5*(SM(2)-SM(4))
    TS(2)=-0.5*(SM(1)-SM(3))
    SM(1)=+0.5*(SM(1)+SM(3))
    SM(2)=+0.5*(SM(2)+SM(4))
    SM(3)=TS(1)
    SM(4)=TS(2)
    027 AQ(1)=QZ(1)-1.57079632679490*(QN+0.5)
    AQ(2)=QZ(2)
    TS(1)=+COS(AQ(1))*0.5*(EXP(+AQ(2))+EXP(-AQ(2)))
    TS(2)=-SIN(AQ(1))*0.5*(EXP(+AQ(2))-EXP(-AQ(2)))
    TM(1)=SM(1)*TS(1)-SM(2)*TS(2)
    TM(2)=SM(1)*TS(2)+SM(2)*TS(1)
    TS(1)=+SIN(AQ(1))*0.5*(EXP(+AQ(2))+EXP(-AQ(2)))
    TS(2)=+COS(AQ(1))*0.5*(EXP(+AQ(2))-EXP(-AQ(2)))
    RM(1)=TM(1)-SM(3)*TS(1)+SM(4)*TS(2)
    RM(2)=TM(2)-SM(3)*TS(2)-SM(4)*TS(1)
    GO TO 029
    028 TM(1)=+2.0*QN*QZ(1)/ZS
    TM(2)=-2.0*QN*QZ(2)/ZS
    TS(1)=TM(1)*RM(1)-TM(2)*RM(2)-RM(3)
    TS(2)=TM(1)*RM(2)+TM(2)*RM(1)-RM(4)
    RM(3)=RM(1)
    RM(4)=RM(2)
    RM(1)=TS(1)
    RM(2)=TS(2)
    QN=QN+1.0
    029 IF(QN.LT.PN)GO TO 028
    030 FJ(1)=QF(1)*RM(1)-QF(2)*RM(2)
    FJ(2)=QF(1)*RM(2)+QF(2)*RM(1)
    W=CMPLX(FJ(1),FJ(2))
    RETURN
    031 SZ(1)=+0.25*(QZ(1)*QZ(1)-QZ(2)*QZ(2))
    SZ(2)=+0.5*QZ(1)*QZ(2)
    AN=QN
    SM(1)=0.0
    SM(2)=0.0
    SM(3)=0.0
    SM(4)=0.0
    TM(1)=1.0
    TM(2)=0.0
    PM=0.0
    032 AN=AN+1.0
    TS(1)=+TM(1)/AN
    TS(2)=+TM(2)/AN
    SM(3)=SM(3)+TS(1)
    SM(4)=SM(4)+TS(2)
    TM(1)=-TS(1)*SZ(1)+TS(2)*SZ(2)
    TM(2)=-TS(1)*SZ(2)-TS(2)*SZ(1)
    PM=PM+1.0
    TM(1)=TM(1)/PM
    TM(2)=TM(2)/PM
    IF(ABS(SM(1))+ABS(TM(1)).NE.ABS(SM(1)))GO TO 033
    IF(ABS(SM(2))+ABS(TM(2)).EQ.ABS(SM(2)))GO TO 034
    033 SM(1)=SM(1)+TM(1)
    SM(2)=SM(2)+TM(2)
    GO TO 032
    034 SM(1)=SM(1)+1.0
    AN=QN+1.0
    SM(3)=AN*SM(3)
    SM(4)=AN*SM(4)
    GO TO 036
    035 AN=QN*(QN+1.0)
    TM(1)=SZ(1)/AN
    TM(2)=SZ(2)/AN
    TS(1)=-TM(1)*SM(3)+TM(2)*SM(4)
    TS(2)=-TM(1)*SM(4)-TM(2)*SM(3)
    SM(3)=SM(1)
    SM(4)=SM(2)
    SM(1)=SM(1)+TS(1)
    SM(2)=SM(2)+TS(2)
    QN=QN-1.0
    036 IF(QN.GT.PN)GO TO 035
    QF(1)=SN
    QF(2)=0.0
    QN=0.0
    GO TO 038
    037 QN=QN+1.0
    TM(1)=QF(1)*QZ(1)-QF(2)*QZ(2)
    TM(2)=QF(1)*QZ(2)+QF(2)*QZ(1)
    QF(1)=0.5*TM(1)/QN
    QF(2)=0.5*TM(2)/QN
    038 IF(QN.LT.PN)GO TO 037
    FJ(1)=QF(1)*SM(1)-QF(2)*SM(2)
    FJ(2)=QF(1)*SM(2)+QF(2)*SM(1)
    W=CMPLX(FJ(1),FJ(2))
    RETURN
END
!
SUBROUTINE ELPFC1 (U,K,L,S,C,D,IERR)
    !     -------------------------------------------------------------
    !     ELPFC1 CALCULATES THE ELLIPTIC FUNCTIONS SN(U,K), CN(U,K),
    !     DN(U,K) FOR COMPLEX U AND REAL MODULUS K.  IT IS ASSUMED THAT
    !     ABS(K) .LE. 1. AND K**2 + L**2 = 1.
    !     -------------------------------------------------------------
    COMPLEX U, S, C, D
    REAL K, L, K2
    !
    U1 = REAL(U)
    U2 = AIMAG(U)
    K2 = K*K
    IF (U1 .EQ. 0.0) GO TO 10
    IF (U2 .NE. 0.0) GO TO 20
    !
    !     CALCULATION FOR U2 = 0.
    !
    CALL ELLPF (U1,K,L,S1,C1,D1,IERR)
    IF (IERR .NE. 0) RETURN
    S2 = 0.0
    C2 = 0.0
    D2 = 0.0
    GO TO 40
    !
    !    CALCULATION FOR U1 = 0.
    !
    10 CALL ELLPF (U2,L,K,S2,C2,D2,IERR)
    IF  (IERR .NE. 0) RETURN
    IF (C2 .EQ. 0.0) GO TO 50
    S1 = 0.0
    S2 = S2/C2
    D1 = D2/C2
    D2 = 0.0
    C1 = 1.0/C2
    C2 = 0.0
    GO TO 40
    !
    !     CALCULATION FOR U1 AND U2 .NE. 0.
    !
    20 CALL ELLPF (U1,K,L,SK,CK,DK,IERR)
    IF (IERR .NE. 0) RETURN
    CALL ELLPF (U2,L,K,SL,CL,DL,IERR)
    IF (IERR .NE. 0) RETURN
    COEF = ABS(K)*SL
    T1 = CL
    T2 = COEF*SK
    TD1 = COEF*T1
    TD2 = COEF*T2
    IF (ABS(T2) .LE. ABS(T1)) GO TO 30
    IF (T2 .EQ. 0.0) GO TO 50
    IF (TD2 .EQ. 0.0) GO TO 50
    T = T1/T2
    R = 1.0/(1.0 + T*T)
    S1 = DL*R/TD2
    S2 = CK*DK*SL*T*R/T2
    C1 = CK*T*R/T2
    C2 = -DK*SL*DL*R/TD2
    D1 = DK*DL*T*R/T2
    D2 = -K2*CK*SL*R/TD2
    GO TO 40
    30 IF (T1 .EQ. 0.0) GO TO 50
    IF (TD1 .EQ. 0.0) GO TO 50
    T = T2/T1
    R = 1.0/(1.0 + T*T)
    S1 = DL*T*R/TD1
    S2 = CK*DK*SL*R/T1
    C1 = CK*R/T1
    C2 = -DK*SL*DL*T*R/TD1
    D1 = DK*DL*R/T1
    D2 = -K2*CK*SL*T*R/TD1
    !
    !     FINAL ASSEMBLY
    !
    40 S = CMPLX (S1, S2)
    C = CMPLX (C1, C2)
    D = CMPLX (D1, D2)
    RETURN
    !
    !     ERROR RETURN
    !
    50 IERR = 3
    RETURN
END
!
SUBROUTINE ELLPF(U,K,L,S,C,D,IERR)
    !     -------------------------------------------------------------
    !     ELLPF CALCULATES THE ELLIPTIC FUNCTIONS SN(U,K), CN(U,K), AND
    !     DN(U,K) FOR REAL U AND REAL MODULUS K.  IT IS ASSUMED THAT
    !     ABS(K) .LE. 1. AND K**2 + L**2 = 1.
    !     -------------------------------------------------------------
    REAL K, L
    DATA PIHALF /1.5707963267949/
    !     ----------------
    !     ****** MAX AND EPS ARE MACHINE DEPENDENT CONSTANTS.
    !            MAX IS THE LARGEST POSITIVE INTEGER THAT MAY
    !            BE USED, AND EPS IS THE SMALLEST REAL NUMBER
    !            FOR WHICH 1 + EPS .GT. 1.
    !
    MAX = IPMPAR(3)
    EPS = SPMPAR(1)
    !
    !     ----------------
    !     CALCULATION FOR L = 0.0
    !
    IF (L .NE. 0.0) GO TO 10
    S = TANH(U)
    E = EXP(-ABS(U))
    C = 2.0*E/(1.0 + E*E)
    D = C
    IERR = 0
    RETURN
    !
    !     CHECK THAT K**2 + L**2 = 1
    !
    10 TOL = 2.0*EPS
    Z = DBLE(K*K) + (DBLE(L*L) - 1.D0)
    IF (ABS(Z) .GT. TOL) GO TO 100
    !
    F = PIHALF
    IF (K .NE. 0.0) CALL ELLPI(PIHALF,0.0,K,L,F,E,IERR)
    F2 = 2.0*F
    !
    !                   ARGUMENT REDUCTION
    !
    U1 = ABS(U)
    R = U1/F2
    IF (R .GE. AMIN1(FLOAT(MAX),1.0/EPS)) GO TO 110
    N = INT(R)
    U1 = U1 - FLOAT(N)*F2
    SG = 1.0
    IF (MOD(N,2) .NE. 0) SG = -1.0
    !
    IF (U1 .LE. 0.0) GO TO 30
    IF (U1 .LE. F) GO TO 20
    U1 = U1 - F2
    SG = -SG
    IF (U1 .GE. 0.0) GO TO 30
    !
    !     CALCULATION OF ELLIPTIC FUNCTIONS FOR 0.0 .LE. U2 .LE. F(K)
    !
    20 U2 = ABS(U1)
    CALL SCD (U2,ABS(K),ABS(L),F,S,C,D)
    IERR = 0
    IF (U1 .LT. 0.0) S = -S
    !
    !     FINAL ASSEMBLY
    !
    S = SG*S
    C = SG*C
    IF (U .LT. 0.0) S = -S
    RETURN
    !
    !             U IS AN INTEGER MULTIPLE OF F2
    !
    30 S = 0.0
    C = SG
    D = 1.0
    IERR = 0
    RETURN
    !
    !                      ERROR RETURN
    !
    100 IERR = 1
    RETURN
    110 IERR = 2
    RETURN
END
!
SUBROUTINE ELLPI (PHI, CPHI, K, L, F, E, IERR)
    !-----------------------------------------------------------------------
    !
    !          REAL ELLIPTIC INTEGRALS OF THE FIRST AND SECOND KINDS
    !
    !                        -----------------
    !
    !     PHI = ARGUMENT                    (0.0 .LE. PHI  .LE. PI/2)
    !     CPHI = PI/2 - PHI                 (0.0 .LE. CPHI .LE. PI/2)
    !     K = MODULUS                       (ABS(K) .LE. 1.0)
    !     L = COMODULUS = SQRT (1 - K*K)    (ABS(L) .LE. 1.0)
    !     F = ELLIPTIC INTEGRAL OF FIRST KIND = F(PHI, K)
    !     E = ELLIPTIC INTEGRAL OF SECOND KIND = E(PHI, K)
    !     IERR = ERROR INDICATOR (IERR = 0  IF NO ERRORS)
    !-----------------------------------------------------------------------
    REAL K, L, K2, L2, LN4
    !------------------------
    !     LN4 = LN(4)
    !     TH1 = TANH(1)
    !------------------------
    DATA LN4/1.3862943611199/
    DATA TH1/.76159415595576/
    !------------------------
    IF (PHI .LT. 0.0 .OR. CPHI .LT. 0.0) GO TO 100
    IF (ABS(K) .GT. 1.0 .OR. ABS(L) .GT. 1.0) GO TO 110
    IERR = 0
    IF (PHI .NE. 0.0) GO TO 10
    F = 0.0
    E = 0.0
    RETURN
    !
    10 IF (PHI .LT. 0.79) GO TO 11
    SN = COS(CPHI)
    CN = SIN(CPHI)
    GO TO 20
    11 SN = SIN(PHI)
    CN = COS(PHI)
    !
    20 K2 = K*K
    L2 = L*L
    SS = SN*SN
    PX = ABS(K*SN)
    QX = ABS(K*CN)
    IF (PX .GE. TH1) GO TO 50
    !
    !     SERIES EXPANSION FOR ABS(K*SIN(PHI)) .LE. TANH(1)
    !
    PN = 1.0
    QN = 2.0
    AN = PHI
    HN = 1.0
    S1 = 0.0
    S2 = 0.0
    TR = PHI*SS
    TS = SN*CN
    !
    30 AN = (PN*AN - TS)/QN
    R  = K2*HN/QN
    S2 = S2 + R*AN
    HN = PN*R
    S0 = S1
    S1 = S1 + HN*AN
    IF (ABS(TR) .LT. ABS(AN)) GO TO 40
    IF (ABS(S1) .LE. ABS(S0)) GO TO 40
    PN = QN + 1.0
    QN = PN + 1.0
    TR = SS*TR
    TS = SS*TS
    GO TO 30
    !
    40 F = PHI + S1
    E = PHI - S2
    RETURN
    !
    !     SERIES EXPANSION FOR ABS(K*SIN(PHI)) .GT. TANH(1)
    !
    50 R = CPABS(L,QX)
    IF (R .EQ. 0.0) GO TO 120
    R2 = R*R
    SI = 1.0
    SJ = 1.0
    SK = 0.0
    RM = 0.0
    RN = 0.0
    S1 = 0.0
    S2 = 0.0
    S3 = 0.0
    S4 = 0.0
    TD = QX*R
    DN = 2.0
    GO TO 70
    !
    60 SI = RI
    SJ = RJ
    SK = RK
    DN = DN + 2.0
    TD = R2*TD
    70 PN = (DN - 1.0)/DN
    QN = (DN + 1.0)/(DN + 2.0)
    RI = PN*SI
    RJ = PN*PN*L2*SJ
    RK = SK + 2.0/(DN*(DN - 1.0))
    R0 = TD/DN
    RM = QN*QN*L2*(RM - R0*RI)
    RN = PN*QN*L2*(RN - R0*SI)
    D1 = RJ
    D2 = QN*RJ
    D3 = RM - RJ*RK
    D4 = RN - PN*L2*SJ*RK + L2*SJ/(DN*DN)
    R0 = S3
    S1 = S1 + D1
    S2 = S2 + D2
    S3 = S3 + D3
    S4 = S4 + D4
    IF (S3 .LT. R0) GO TO 60
    !
    W = 1.0 + PX
    P = LN4 - ALOG(R + QX)
    T1 = (1.0 + S1)*P + QX/R*ALNREL(-0.5*R2/W)
    T2 = (0.5 + S2)*L2*P + (1.0 - QX*R/W)
    F = T1 + S3
    E = T2 + S4
    RETURN
    !
    !     ERROR RETURN
    !
    100 IERR = 1
    RETURN
    110 IERR = 2
    RETURN
    120 IERR = 3
    RETURN
END
!
SUBROUTINE SCD(U,K,L,F,S,C,D)
    !     --------------------------------------------------------
    !     SCD COMPUTES THE ELLIPTIC FUNCTIONS SN(U,K), CN(U,K),
    !     AND DN(U,K) FOR REAL U AND REAL MODULUS K SUCH THAT
    !     0.0 .LE. U .LE. F AND 0.0 .LE. K .LT. 1.0, WHERE
    !     F = F(K) IS THE COMPLETE ELLIPTIC INTEGRAL OF THE
    !     FIRST KIND, AND F1 = F(L) IS THE COMPLEMENTARY INTEGRAL.
    !     IT IS ASSUMED THAT K**2 + L**2 = 1.
    !     --------------------------------------------------------
    REAL K, L
    DATA PIHALF /1.5707963267949/
    !     ------------------------
    IF (K .EQ. 0.0) GO TO 40
    V = F - U
    !
    !     USES MACLAURIN EXPANSION WHEN U OR V .LE. 0.01
    !
    IF (U .GT. 0.01) GO TO 10
    CALL SCDM (U,K,S,C,D)
    RETURN
    10 IF (V .GT. 0.01) GO TO 20
    CALL SCDM (V,K,S1,C1,D1)
    S = C1/D1
    C = L*S1/D1
    D = L/D1
    RETURN
    !
    !     USES FOURIER EXPANSION WHEN K .LE. L
    !
    20 CALL ELLPI(PIHALF,0.0,L,K,F1,E1,IERR)
    !
    IF (K .GT. L) GO TO 30
    CALL SCDF (U,K,L,F,F1,S,C,D)
    RETURN
    !
    !     USES IMAGINARY TRANSFORMATION OF JACOBI AND FOURIER
    !     EXPANSION WHEN K .GT. L
    !
    30 CALL SCDJ (U,K,L,F,F1,S,C,D)
    RETURN
    !
    !     COMPUTATION FOR K = 0.0
    !
    40 S = SIN(U)
    C = COS(U)
    D = 1.0
    RETURN
END
SUBROUTINE SCDM(U,K,S,C,D)
    !     -------------------------------------------------
    !     CALCULATES SN(U,K), CN(U,K), AND DN(U,K) FOR
    !     0.0 .LE. U .LE. 0.01 AND FOR 0.0 .LE. K .LE. 1.0
    !     BY USE OF THE MACLAURIN EXPANSION FOR SN(U,K)
    !     -------------------------------------------------
    REAL K, K2
    !
    K2 = K*K
    U2 = U*U
    C1 = -(1.0 + K2)/6.0
    C2 = (1.0 + K2*(14.0 + K2))/120.0
    C3 = -(1.0 + K2*(135.0 + K2*(135.0 + K2)))/5040.0
    C4 = (1.0 + K2*(1228.0 + K2*(5478.0 + K2*(1228.0 + K2))))/362880.0
    S = U*(1.0 + U2*(C1 + U2*(C2 + U2*(C3 + C4*U2))))
    C = SQRT(1.0 - S*S)
    D = SQRT(1.0 - (K*S)**2)
    RETURN
END
SUBROUTINE SCDF(U,K,L,F,F1,S,C,D)
    !     -------------------------------------------------------------
    !     SCDF COMPUTES SN(U,K), CN(U,K), AND DN(U,K) FOR REAL U AND
    !     K BY USE OF THE FOURIER EXPANSION FOR SN(U,K).  IT IS
    !     ASSUMED THAT 0.0 .LE. K .LT. 1.0 AND 0.0 .LE. U .LE. F,
    !     WHERE F = F(K) IS THE COMPLETE ELLIPTIC INTEGRAL OF THE
    !     FIRST KIND AND F1 = F(L) IS THE COMPLEMENTARY INTEGRAL, WITH
    !     L .NE. 0. AND K**2 + L**2 = 1.
    !     -------------------------------------------------------------
    REAL I, K, L
    DATA PIHALF /1.5707963267949/
    !     -------------------------------------------------
    !     EPS IS A MACHINE DEPENDENT CONSTANT.  EPS IS THE
    !     SMALLEST NUMBER SUCH THAT 1. + EPS .GT. 1.
    !
    EPS = SPMPAR(1)
    !
    !     -------------------------------------------------
    TOL = EPS/10.0
    V = F - U
    QH = EXP(-PIHALF*F1/F)
    Q1 = QH*QH
    Q2 = Q1*Q1
    COEF = 4.*PIHALF*QH/(K*F)
    QN = 1.0
    QD = Q1
    W = AMIN1(U,V)
    X = PIHALF*W/F
    !
    !     CALCULATION OF SERIES FOR W = AMIN1(U,V)
    !
    I = 1.0
    SUM = 0.0
    10 AI = QN/(1.0 - QD)
    A = AI*SIN(I*X)
    SUM = SUM + A
    IF (ABS(AI) .LT. TOL*ABS(SUM)) GO TO 20
    QN = QN*Q1
    QD = QD*Q2
    I = I + 2.0
    GO TO 10
    !
    !     ASSEMBLY FOR U .LE. V
    !
    20 S = COEF*SUM
    C = SQRT(1.0 - S*S)
    D = SQRT(1.0 - (K*S)**2)
    IF (U .EQ. W) RETURN
    !
    !     ASSEMBLY FOR U .GT. V
    !
    TEMP = S
    S = C/D
    C = L*TEMP/D
    D = L/D
    RETURN
END
SUBROUTINE SCDJ(U,K,L,F,F1,S,C,D)
    !     ----------------------------------------------------------------
    !     SCDJ COMPUTES SN(U,K), CN(U,K), AND DN(U,K) FOR REAL U AND
    !     K USING THE IMAGINARY TRANSFORMATION OF JACOBI AND A
    !     FOURIER EXPANSION.  IT IS ASSUMED THAT 0.0 .LE. K .LT. 1.0
    !     AND 0.0 .LE. U .LE. F, WHERE F = F(K) IS THE COMPLETE ELLIPTIC
    !     INTEGRAL OF THE FIRST KIND AND F1 = F(L) IS THE COMPLEMENTARY
    !     INTEGRAL, AND THAT L .NE. 0. AND K**2 + L**2 = 1.
    !     ----------------------------------------------------------------
    REAL K, L, N
    DATA PIHALF /1.5707963267949/
    DATA PI /3.1415926535898/
    !     ------------------------------------------------
    !     EPS IS A MACHINE DEPENDENT CONSTANT.  EPS IS THE
    !     SMALLEST NUMBER SUCH THAT 1. + EPS .GT. 1.
    !
    EPS = SPMPAR(1)
    !
    !     ------------------------------------------------
    TOL = EPS/10.0
    V = F - U
    Q1 = -EXP(-PI*F/F1)
    Q2 = Q1*Q1
    !
    W = AMIN1(U,V)
    E1 = PI*AMAX1(U,V)/F1
    E2 = PI*(F + W)/F1
    E1 = EXP(-E1)
    E2 = EXP(-E2)
    !
    COEF = PIHALF/(K*F1)
    X = PIHALF*W/F1
    X2 = 2.0*X
    !
    !     CALCULATION OF SERIES FOR W = AMIN1(U,V)
    !
    N = 1.0
    Q1N = Q1
    Q2N = Q2
    E1N = E1
    E2N = E2
    SUM = 0.0
    !
    20 XN = N*X2
    IF (XN .GT. 1.0) GO TO 30
    CALL SNHCSH(SH,CH,XN,-1)
    SH = SH + XN
    A = 2.0*Q1N*ABS(Q1N)*SH/(1.0 + Q2N)
    GO TO 40
    30 A = Q1N*(E1N - E2N)/(1.0 + Q2N)
    40 SUM = SUM + A
    IF (ABS(A) .LT. TOL*ABS(SUM)) GO TO 50
    Q1N = Q1N*Q1
    Q2N = Q2N*Q2
    E1N = E1N*E1
    E2N = E2N*E2
    N = N + 1.0
    GO TO 20
    !
    !     ASSEMBLY FOR U .LE. V
    !
    50 S = COEF*(TANH(X) + 2.0*SUM)
    C = SQRT(1.0 - S*S)
    D = SQRT(1.0 - (K*S)**2)
    IF (U .EQ. W) RETURN
    !
    !     ASSEMBLY FOR U .GT. V
    !
    TEMP = S
    S = C/D
    C = L*TEMP/D
    D = L/D
    RETURN
END
!
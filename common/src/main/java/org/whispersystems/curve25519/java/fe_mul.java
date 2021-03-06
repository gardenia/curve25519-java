package org.whispersystems.curve25519.java;

public class fe_mul {

//CONVERT #include "fe.h"
//CONVERT #include "long.h"

/*
h = f * g
Can overlap h with f or g.

Preconditions:
   |f| bounded by 1.65*2^26,1.65*2^25,1.65*2^26,1.65*2^25,etc.
   |g| bounded by 1.65*2^26,1.65*2^25,1.65*2^26,1.65*2^25,etc.

Postconditions:
   |h| bounded by 1.01*2^25,1.01*2^24,1.01*2^25,1.01*2^24,etc.
*/

/*
Notes on implementation strategy:

Using schoolbook multiplication.
Karatsuba would save a little in some cost models.

Most multiplications by 2 and 19 are 32-bit precomputations;
cheaper than 64-bit postcomputations.

There is one remaining multiplication by 19 in the carry chain;
one *19 precomputation can be merged into this,
but the resulting data flow is considerably less clean.

There are 12 carries below.
10 of them are 2-way parallelizable and vectorizable.
Can get away with 11 carries, but then data flow is much deeper.

With tighter constraints on inputs can squeeze carries into int32.
*/

public static void fe_mul(int[] h,int[] f,int[] g)
{
  int g1_19 = 19 * g[1]; /* 1.959375*2^29 */
  int g2_19 = 19 * g[2]; /* 1.959375*2^30; still ok */
  int g3_19 = 19 * g[3];
  int g4_19 = 19 * g[4];
  int g5_19 = 19 * g[5];
  int g6_19 = 19 * g[6];
  int g7_19 = 19 * g[7];
  int g8_19 = 19 * g[8];
  int g9_19 = 19 * g[9];
  int f1_2 = 2 * f[1];
  int f3_2 = 2 * f[3];
  int f5_2 = 2 * f[5];
  int f7_2 = 2 * f[7];
  int f9_2 = 2 * f[9];
  long f0g0    = f[0] * (long) g[0];
  long f0g1    = f[0] * (long) g[1];
  long f0g2    = f[0] * (long) g[2];
  long f0g3    = f[0] * (long) g[3];
  long f0g4    = f[0] * (long) g[4];
  long f0g5    = f[0] * (long) g[5];
  long f0g6    = f[0] * (long) g[6];
  long f0g7    = f[0] * (long) g[7];
  long f0g8    = f[0] * (long) g[8];
  long f0g9    = f[0] * (long) g[9];
  long f1g0    = f[1] * (long) g[0];
  long f1g1_2  = f1_2 * (long) g[1];
  long f1g2    = f[1] * (long) g[2];
  long f1g3_2  = f1_2 * (long) g[3];
  long f1g4    = f[1] * (long) g[4];
  long f1g5_2  = f1_2 * (long) g[5];
  long f1g6    = f[1] * (long) g[6];
  long f1g7_2  = f1_2 * (long) g[7];
  long f1g8    = f[1] * (long) g[8];
  long f1g9_38 = f1_2 * (long) g9_19;
  long f2g0    = f[2] * (long) g[0];
  long f2g1    = f[2] * (long) g[1];
  long f2g2    = f[2] * (long) g[2];
  long f2g3    = f[2] * (long) g[3];
  long f2g4    = f[2] * (long) g[4];
  long f2g5    = f[2] * (long) g[5];
  long f2g6    = f[2] * (long) g[6];
  long f2g7    = f[2] * (long) g[7];
  long f2g8_19 = f[2] * (long) g8_19;
  long f2g9_19 = f[2] * (long) g9_19;
  long f3g0    = f[3] * (long) g[0];
  long f3g1_2  = f3_2 * (long) g[1];
  long f3g2    = f[3] * (long) g[2];
  long f3g3_2  = f3_2 * (long) g[3];
  long f3g4    = f[3] * (long) g[4];
  long f3g5_2  = f3_2 * (long) g[5];
  long f3g6    = f[3] * (long) g[6];
  long f3g7_38 = f3_2 * (long) g7_19;
  long f3g8_19 = f[3] * (long) g8_19;
  long f3g9_38 = f3_2 * (long) g9_19;
  long f4g0    = f[4] * (long) g[0];
  long f4g1    = f[4] * (long) g[1];
  long f4g2    = f[4] * (long) g[2];
  long f4g3    = f[4] * (long) g[3];
  long f4g4    = f[4] * (long) g[4];
  long f4g5    = f[4] * (long) g[5];
  long f4g6_19 = f[4] * (long) g6_19;
  long f4g7_19 = f[4] * (long) g7_19;
  long f4g8_19 = f[4] * (long) g8_19;
  long f4g9_19 = f[4] * (long) g9_19;
  long f5g0    = f[5] * (long) g[0];
  long f5g1_2  = f5_2 * (long) g[1];
  long f5g2    = f[5] * (long) g[2];
  long f5g3_2  = f5_2 * (long) g[3];
  long f5g4    = f[5] * (long) g[4];
  long f5g5_38 = f5_2 * (long) g5_19;
  long f5g6_19 = f[5] * (long) g6_19;
  long f5g7_38 = f5_2 * (long) g7_19;
  long f5g8_19 = f[5] * (long) g8_19;
  long f5g9_38 = f5_2 * (long) g9_19;
  long f6g0    = f[6] * (long) g[0];
  long f6g1    = f[6] * (long) g[1];
  long f6g2    = f[6] * (long) g[2];
  long f6g3    = f[6] * (long) g[3];
  long f6g4_19 = f[6] * (long) g4_19;
  long f6g5_19 = f[6] * (long) g5_19;
  long f6g6_19 = f[6] * (long) g6_19;
  long f6g7_19 = f[6] * (long) g7_19;
  long f6g8_19 = f[6] * (long) g8_19;
  long f6g9_19 = f[6] * (long) g9_19;
  long f7g0    = f[7] * (long) g[0];
  long f7g1_2  = f7_2 * (long) g[1];
  long f7g2    = f[7] * (long) g[2];
  long f7g3_38 = f7_2 * (long) g3_19;
  long f7g4_19 = f[7] * (long) g4_19;
  long f7g5_38 = f7_2 * (long) g5_19;
  long f7g6_19 = f[7] * (long) g6_19;
  long f7g7_38 = f7_2 * (long) g7_19;
  long f7g8_19 = f[7] * (long) g8_19;
  long f7g9_38 = f7_2 * (long) g9_19;
  long f8g0    = f[8] * (long) g[0];
  long f8g1    = f[8] * (long) g[1];
  long f8g2_19 = f[8] * (long) g2_19;
  long f8g3_19 = f[8] * (long) g3_19;
  long f8g4_19 = f[8] * (long) g4_19;
  long f8g5_19 = f[8] * (long) g5_19;
  long f8g6_19 = f[8] * (long) g6_19;
  long f8g7_19 = f[8] * (long) g7_19;
  long f8g8_19 = f[8] * (long) g8_19;
  long f8g9_19 = f[8] * (long) g9_19;
  long f9g0    = f[9] * (long) g[0];
  long f9g1_38 = f9_2 * (long) g1_19;
  long f9g2_19 = f[9] * (long) g2_19;
  long f9g3_38 = f9_2 * (long) g3_19;
  long f9g4_19 = f[9] * (long) g4_19;
  long f9g5_38 = f9_2 * (long) g5_19;
  long f9g6_19 = f[9] * (long) g6_19;
  long f9g7_38 = f9_2 * (long) g7_19;
  long f9g8_19 = f[9] * (long) g8_19;
  long f9g9_38 = f9_2 * (long) g9_19;
  long h0 = f0g0+f1g9_38+f2g8_19+f3g7_38+f4g6_19+f5g5_38+f6g4_19+f7g3_38+f8g2_19+f9g1_38;
  long h1 = f0g1+f1g0   +f2g9_19+f3g8_19+f4g7_19+f5g6_19+f6g5_19+f7g4_19+f8g3_19+f9g2_19;
  long h2 = f0g2+f1g1_2 +f2g0   +f3g9_38+f4g8_19+f5g7_38+f6g6_19+f7g5_38+f8g4_19+f9g3_38;
  long h3 = f0g3+f1g2   +f2g1   +f3g0   +f4g9_19+f5g8_19+f6g7_19+f7g6_19+f8g5_19+f9g4_19;
  long h4 = f0g4+f1g3_2 +f2g2   +f3g1_2 +f4g0   +f5g9_38+f6g8_19+f7g7_38+f8g6_19+f9g5_38;
  long h5 = f0g5+f1g4   +f2g3   +f3g2   +f4g1   +f5g0   +f6g9_19+f7g8_19+f8g7_19+f9g6_19;
  long h6 = f0g6+f1g5_2 +f2g4   +f3g3_2 +f4g2   +f5g1_2 +f6g0   +f7g9_38+f8g8_19+f9g7_38;
  long h7 = f0g7+f1g6   +f2g5   +f3g4   +f4g3   +f5g2   +f6g1   +f7g0   +f8g9_19+f9g8_19;
  long h8 = f0g8+f1g7_2 +f2g6   +f3g5_2 +f4g4   +f5g3_2 +f6g2   +f7g1_2 +f8g0   +f9g9_38;
  long h9 = f0g9+f1g8   +f2g7   +f3g6   +f4g5   +f5g4   +f6g3   +f7g2   +f8g1   +f9g0   ;
  long carry0;
  long carry1;
  long carry2;
  long carry3;
  long carry4;
  long carry5;
  long carry6;
  long carry7;
  long carry8;
  long carry9;

  /*
  |h0| <= (1.65*1.65*2^52*(1+19+19+19+19)+1.65*1.65*2^50*(38+38+38+38+38))
    i.e. |h0| <= 1.4*2^60; narrower ranges for h2, h4, h6, h8
  |h1| <= (1.65*1.65*2^51*(1+1+19+19+19+19+19+19+19+19))
    i.e. |h1| <= 1.7*2^59; narrower ranges for h3, h5, h7, h9
  */

  carry0 = (h0 + (long) (1<<25)) >> 26; h1 += carry0; h0 -= carry0 << 26;
  carry4 = (h4 + (long) (1<<25)) >> 26; h5 += carry4; h4 -= carry4 << 26;
  /* |h0| <= 2^25 */
  /* |h4| <= 2^25 */
  /* |h1| <= 1.71*2^59 */
  /* |h5| <= 1.71*2^59 */

  carry1 = (h1 + (long) (1<<24)) >> 25; h2 += carry1; h1 -= carry1 << 25;
  carry5 = (h5 + (long) (1<<24)) >> 25; h6 += carry5; h5 -= carry5 << 25;
  /* |h1| <= 2^24; from now on fits into int32 */
  /* |h5| <= 2^24; from now on fits into int32 */
  /* |h2| <= 1.41*2^60 */
  /* |h6| <= 1.41*2^60 */

  carry2 = (h2 + (long) (1<<25)) >> 26; h3 += carry2; h2 -= carry2 << 26;
  carry6 = (h6 + (long) (1<<25)) >> 26; h7 += carry6; h6 -= carry6 << 26;
  /* |h2| <= 2^25; from now on fits into int32 unchanged */
  /* |h6| <= 2^25; from now on fits into int32 unchanged */
  /* |h3| <= 1.71*2^59 */
  /* |h7| <= 1.71*2^59 */

  carry3 = (h3 + (long) (1<<24)) >> 25; h4 += carry3; h3 -= carry3 << 25;
  carry7 = (h7 + (long) (1<<24)) >> 25; h8 += carry7; h7 -= carry7 << 25;
  /* |h3| <= 2^24; from now on fits into int32 unchanged */
  /* |h7| <= 2^24; from now on fits into int32 unchanged */
  /* |h4| <= 1.72*2^34 */
  /* |h8| <= 1.41*2^60 */

  carry4 = (h4 + (long) (1<<25)) >> 26; h5 += carry4; h4 -= carry4 << 26;
  carry8 = (h8 + (long) (1<<25)) >> 26; h9 += carry8; h8 -= carry8 << 26;
  /* |h4| <= 2^25; from now on fits into int32 unchanged */
  /* |h8| <= 2^25; from now on fits into int32 unchanged */
  /* |h5| <= 1.01*2^24 */
  /* |h9| <= 1.71*2^59 */

  carry9 = (h9 + (long) (1<<24)) >> 25; h0 += carry9 * 19; h9 -= carry9 << 25;
  /* |h9| <= 2^24; from now on fits into int32 unchanged */
  /* |h0| <= 1.1*2^39 */

  carry0 = (h0 + (long) (1<<25)) >> 26; h1 += carry0; h0 -= carry0 << 26;
  /* |h0| <= 2^25; from now on fits into int32 unchanged */
  /* |h1| <= 1.01*2^24 */

  h[0] = (int)h0;
  h[1] = (int)h1;
  h[2] = (int)h2;
  h[3] = (int)h3;
  h[4] = (int)h4;
  h[5] = (int)h5;
  h[6] = (int)h6;
  h[7] = (int)h7;
  h[8] = (int)h8;
  h[9] = (int)h9;
}


}

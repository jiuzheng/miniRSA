/**
 * 
 */
package miniRSAChat;

/**
 * @author Jie Qi
 *
 */
public class RSA {
	// Public key contains N and e
	public static class PublicKey {
		public final long N;
		public final long e;
		PublicKey(long N, long e) {
			this.N = N;
			this.e = e;
		}
	}


}

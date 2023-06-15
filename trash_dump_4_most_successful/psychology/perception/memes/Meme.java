package psychology.perception.memes;

public interface Meme {

	public String printDescription();

	/**
	 * This meme simply indicates that something is known in the culture, such as a
	 * social construct
	 * 
	 * @author borah
	 *
	 */
	public static enum Known implements Meme {
		KNOWN;

		@Override
		public String printDescription() {
			return "is known";
		}
	}
}

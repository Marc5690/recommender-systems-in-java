/**
 * A class to represent a review
 * 
 * Michael O'Mahony
 * 20/02/2013
 */

package util;

public class Review {
	private String reviewId;
	private String reviewerUrl;
	
	private String productCategory;
	private String productId;
	private String productName;
	
	private String reviewTitle;
	private String reviewText;
	private double reviewRating;
	private String reviewDate;
	
	private int posVotes;
	private int totalVotes;
	private double helpfulness;
	
	/**
	 * default constructor - creates a new Review object
	 */
	public Review()
	{}
	
	/**
	 * constructor - creates a new Review object
	 */
	public Review(final String reviewId, final String reviewerUrl, final String productCategory, final String productId, final String productName, final String reviewTitle, final String reviewText, final double reviewRating, final String reviewDate, final int posVotes, final int totalVotes, final double helpfulness)
	{
		this.reviewId = reviewId;
		this.reviewerUrl = reviewerUrl;
		
		this.productCategory = productCategory;
		this.productId = productId;
		this.productName = productName;
		
		this.reviewTitle = reviewTitle;
		this.reviewText = reviewText;
		this.reviewRating = reviewRating;
		this.reviewDate = reviewDate;
		
		this.posVotes = posVotes;
		this.totalVotes = totalVotes;
		this.helpfulness = helpfulness;
	}
	
	/**
	 * @return the reviewId
	 */
	public String getReviewId() {
		return reviewId;
	}
	
	/**
	 * @param reviewId the reviewId to set
	 */
	public void setReviewId(final String reviewId) {
		this.reviewId = reviewId;
	}
	
	/**
	 * @return the reviewerUrl
	 */
	public String getReviewerUrl() {
		return reviewerUrl;
	}
	
	/**
	 * @param reviewerUrl the reviewerUrl to set
	 */
	public void setReviewerUrl(final String reviewerUrl) {
		this.reviewerUrl = reviewerUrl;
	}
	
	/**
	 * @return the productCategory
	 */
	public String getProductCategory() {
		return productCategory;
	}
	
	/**
	 * @param productCategory the productCategory to set
	 */
	public void setProductCategory(final String productCategory) {
		this.productCategory = productCategory;
	}
	
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(final String productId) {
		this.productId = productId;
	}
	
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(final String productName) {
		this.productName = productName;
	}
	
	/**
	 * @return the reviewTitle
	 */
	public String getReviewTitle() {
		return reviewTitle;
	}
	
	/**
	 * @param reviewTitle the reviewTitle to set
	 */
	public void setReviewTitle(final String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}
	
	/**
	 * @return the reviewText
	 */
	public String getReviewText() {
		return reviewText;
	}
	
	/**
	 * @param reviewText the reviewText to set
	 */
	public void setReviewText(final String reviewText) {
		this.reviewText = reviewText;
	}
	
	/**
	 * @return the reviewRating
	 */
	public double getReviewRating() {
		return reviewRating;
	}
	
	/**
	 * @param reviewRating the reviewRating to set
	 */
	public void setReviewRating(final double reviewRating) {
		this.reviewRating = reviewRating;
	}
	
	/**
	 * @return the reviewDate
	 */
	public String getReviewDate() {
		return reviewDate;
	}
	
	/**
	 * @param reviewDate the reviewDate to set
	 */
	public void setReviewDate(final String reviewDate) {
		this.reviewDate = reviewDate;
	}
	
	/**
	 * @return the posVotes
	 */
	public int getPosVotes() {
		return posVotes;
	}
	
	/**
	 * @param posVotes the posVotes to set
	 */
	public void setPosVotes(final int posVotes) {
		this.posVotes = posVotes;
	}
	
	/**
	 * @return the totalVotes
	 */
	public int getTotalVotes() {
		return totalVotes;
	}
	
	/**
	 * @param totalVotes the totalVotes to set
	 */
	public void setTotalVotes(final int totalVotes) {
		this.totalVotes = totalVotes;
	}
	
	/**
	 * @return the helpfulness
	 */
	public double getHelpfulness() {
		return helpfulness;
	}
	
	/**
	 * @param helpfulness the helpfulness to set
	 */
	public void setHelpfulness(final double helpfulness) {
		this.helpfulness = helpfulness;
	}
	
	/**
	 * @return a string representation of the object
	 */
	public String toString()
	{
		return "review id: " + reviewId + "\nreviewer url: " + reviewerUrl + "\nproduct category: " + productCategory + "\nproduct id: " + productId + "\nproduct name: " + productName + "\nreview title: " + reviewTitle + "\nreview text: " + reviewText + "\nreview rating: " + reviewRating + "\nreview date: " + reviewDate + "\n# pos. helpfulness votes: " + posVotes + "\ntotal # helpfulness votes: " + totalVotes + "\nhelpfulness: " + helpfulness;

	}
}

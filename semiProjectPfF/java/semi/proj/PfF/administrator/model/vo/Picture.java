package semi.proj.PfF.administrator.model.vo;

public class Picture {
	private int pictureNo;
	private String pictureURL;
	private String originName;
	private String newName;
	private int pictureCategory;
	private int pictureRefBoardNo;
	public Picture() {
		super();
	}
	public Picture(int pictureNo, String pictureURL, String originName, String newName, int pictureCategory,
			int pictureRefBoardNo) {
		super();
		this.pictureNo = pictureNo;
		this.pictureURL = pictureURL;
		this.originName = originName;
		this.newName = newName;
		this.pictureCategory = pictureCategory;
		this.pictureRefBoardNo = pictureRefBoardNo;
	}
	public int getPictureNo() {
		return pictureNo;
	}
	public void setPictureNo(int pictureNo) {
		this.pictureNo = pictureNo;
	}
	public String getPictureURL() {
		return pictureURL;
	}
	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
	public String getOriginName() {
		return originName;
	}
	public void setOriginName(String originName) {
		this.originName = originName;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public int getPictureCategory() {
		return pictureCategory;
	}
	public void setPictureCategory(int pictureCategory) {
		this.pictureCategory = pictureCategory;
	}
	public int getPictureRefBoardNo() {
		return pictureRefBoardNo;
	}
	public void setPictureRefBoardNo(int pictureRefBoardNo) {
		this.pictureRefBoardNo = pictureRefBoardNo;
	}
	@Override
	public String toString() {
		return "Picture [pictureNo=" + pictureNo + ", pictureURL=" + pictureURL + ", originName=" + originName
				+ ", newName=" + newName + ", pictureCategory=" + pictureCategory + ", pictureRefBoardNo="
				+ pictureRefBoardNo + "]";
	}
	
}

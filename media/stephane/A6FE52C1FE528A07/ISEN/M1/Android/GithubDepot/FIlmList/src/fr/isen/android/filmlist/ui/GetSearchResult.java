package fr.isen.android.filmlist.ui;

public class GetSearchResult {
	
	private static GetSearchResult instance = null;
	private SearchResultsFragment searchResultFragment;
	
	private GetSearchResult(){
		searchResultFragment = null;
	}
	
	public static GetSearchResult getInstance(){
		if(instance == null){
			instance = new GetSearchResult();
		}
		return instance;
	}

	public SearchResultsFragment getSearchResultFragment() {
		return searchResultFragment;
	}

	public void setSearchResultFragment(SearchResultsFragment searchResultFragment) {
		this.searchResultFragment = searchResultFragment;
	}
	
}

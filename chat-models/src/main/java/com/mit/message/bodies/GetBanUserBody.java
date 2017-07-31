package com.mit.message.bodies;

/**
 * Created by Hung on 6/24/2017.
 */
public class GetBanUserBody {
    private int page;
    private int count;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

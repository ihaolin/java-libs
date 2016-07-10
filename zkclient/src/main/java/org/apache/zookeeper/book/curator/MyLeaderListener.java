package org.apache.zookeeper.book.curator;

import org.apache.curator.framework.recipes.leader.LeaderLatchListener;

/**
 * Created by haolin on 4/12/15.
 */
public class MyLeaderListener implements LeaderLatchListener {

    /**
     * 成为了Leader时调用
     */
    @Override
    public void isLeader() {

    }

    /**
     * 未成为Leader时调用
     */
    @Override
    public void notLeader() {

    }
}

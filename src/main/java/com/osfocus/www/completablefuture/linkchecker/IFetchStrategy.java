package com.osfocus.www.completablefuture.linkchecker;

import java.util.List;

public interface IFetchStrategy {
    List<ReferenceInfo> fetch(String url);
}

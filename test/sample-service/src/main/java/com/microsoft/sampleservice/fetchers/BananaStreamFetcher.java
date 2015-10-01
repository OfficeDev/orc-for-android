package com.microsoft.sampleservice.fetchers;

import com.microsoft.services.orc.core.DependencyResolver;
import com.microsoft.services.orc.core.OrcExecutable;
import com.microsoft.services.orc.core.OrcStreamFetcher;

public class BananaStreamFetcher extends OrcStreamFetcher {

    public BananaStreamFetcher(String urlComponent, OrcExecutable parent, DependencyResolver dependencyResolver) {
        super(urlComponent, parent, dependencyResolver);

    }
}

package gov.ismonnet.client.table;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public abstract class TableModule {

    @Binds @Singleton abstract Table table(TableImpl impl);
}

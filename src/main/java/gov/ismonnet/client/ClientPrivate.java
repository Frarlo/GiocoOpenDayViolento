package gov.ismonnet.client;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Qualifier
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@interface ClientPrivate {
}

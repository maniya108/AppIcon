#import <Cordova/CDVPlugin.h>

@interface AppIcon : CDVPlugin

- (void)isSupported:(CDVInvokedUrlCommand*)command;
- (void)getName:(CDVInvokedUrlCommand*)command;
- (void)change:(CDVInvokedUrlCommand*)command;
- (void)reset:(CDVInvokedUrlCommand*)command;

@end

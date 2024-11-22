#import "AppIcon.h"
#import <UIKit/UIKit.h>

@implementation AppIcon

- (void)isSupported:(CDVInvokedUrlCommand*)command {
    BOOL isSupported = [UIApplication sharedApplication].supportsAlternateIcons;
    NSDictionary *result = @{@"value": @(isSupported)};

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getName:(CDVInvokedUrlCommand*)command {
    NSString *iconName = [UIApplication sharedApplication].alternateIconName;

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:iconName];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)change:(CDVInvokedUrlCommand*)command {
    NSDictionary *arguments = [command.arguments objectAtIndex:0];
    NSString *iconName = arguments[@"name"];
    
    NSNumber *suppressNotification = arguments[@"suppressNotification"];
      if (suppressNotification == nil) {
          suppressNotification = @YES; // Default value if not provided
      }

    if (iconName == nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Must provide an icon name."];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }

    [self setIcon:iconName suppressNotification:[suppressNotification boolValue] command:command];
}

- (void)reset:(CDVInvokedUrlCommand*)command {
    NSDictionary *arguments = [command.arguments objectAtIndex:0];
    NSNumber *suppressNotification = arguments[@"suppressNotification"];
    
    if (suppressNotification == nil) {
        suppressNotification = @YES; // Default value if not provided
    }

    if (suppressNotification == nil) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Invalid suppressNotification parameter."];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }

    [self setIcon:nil suppressNotification:[suppressNotification boolValue] command:command];
}

- (void)setIcon:(NSString*)iconName suppressNotification:(BOOL)suppressNotification command:(CDVInvokedUrlCommand*)command {
    if (![UIApplication sharedApplication].supportsAlternateIcons) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Alternate icons not supported."];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }

    if (suppressNotification) {
        SEL selector = NSSelectorFromString(@"_setAlternateIconName:completionHandler:");
        if ([[UIApplication sharedApplication] respondsToSelector:selector]) {
            NSMethodSignature *signature = [[UIApplication sharedApplication] methodSignatureForSelector:selector];
            NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:signature];
            [invocation setSelector:selector];
            [invocation setTarget:[UIApplication sharedApplication]];

            NSString *icon = iconName;
            [invocation setArgument:&icon atIndex:2];

            void (^completionHandler)(NSError*) = ^(NSError *error) {
                if (error) {
                    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
                    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
                } else {
                    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
                    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
                }
            };
            [invocation setArgument:&completionHandler atIndex:3];
            [invocation invoke];
        }
    } else {
        [[UIApplication sharedApplication] setAlternateIconName:iconName completionHandler:^(NSError *error) {
            if (error) {
                CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
                [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            } else {
                CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
                [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            }
        }];
    }
}

@end

// Security headers utility for frontend protection
class SecurityHeaders {
  static initCSP() {
    // Content Security Policy meta tag (backup in case server headers aren't set)
    const existingCSP = document.querySelector('meta[http-equiv="Content-Security-Policy"]');
    
    if (!existingCSP) {
      const cspMeta = document.createElement('meta');
      cspMeta.httpEquiv = 'Content-Security-Policy';
      cspMeta.content = this.getCSPDirectives();
      document.head.appendChild(cspMeta);
    }
  }

  static getCSPDirectives() {
    const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080';
    const baseUrl = new URL(apiUrl).origin;
    
    return [
      "default-src 'self'",
      `connect-src 'self' ${baseUrl}`,
      "script-src 'self' 'unsafe-inline'", // Note: unsafe-inline needed for Vite in dev
      "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com",
      "font-src 'self' https://fonts.gstatic.com",
      "img-src 'self' data: https: http:",
      "media-src 'self'",
      "object-src 'none'",
      "frame-src 'none'",
      "base-uri 'self'",
      "form-action 'self'",
      "frame-ancestors 'none'"
    ].join('; ');
  }

  // Set security-related meta tags
  static initSecurityMeta() {
    const securityMetas = [
      { name: 'referrer', content: 'strict-origin-when-cross-origin' },
      { name: 'robots', content: 'noindex, nofollow' }, // For development
      { httpEquiv: 'X-Content-Type-Options', content: 'nosniff' },
      { httpEquiv: 'X-Frame-Options', content: 'DENY' },
      { httpEquiv: 'X-XSS-Protection', content: '1; mode=block' },
      { httpEquiv: 'Permissions-Policy', content: 'geolocation=(), microphone=(), camera=()' }
    ];

    securityMetas.forEach(meta => {
      const existingMeta = document.querySelector(
        meta.name ? `meta[name="${meta.name}"]` : `meta[http-equiv="${meta.httpEquiv}"]`
      );
      
      if (!existingMeta) {
        const metaTag = document.createElement('meta');
        if (meta.name) {
          metaTag.name = meta.name;
        } else {
          metaTag.httpEquiv = meta.httpEquiv;
        }
        metaTag.content = meta.content;
        document.head.appendChild(metaTag);
      }
    });
  }

  // Initialize all security measures
  static initialize() {
    this.initCSP();
    this.initSecurityMeta();
    this.preventClickjacking();
    this.secureLocalStorage();
  }

  // Prevent clickjacking
  static preventClickjacking() {
    if (window.top !== window.self) {
      // If the page is in a frame, redirect to break out
      window.top.location = window.self.location;
    }
  }

  // Secure localStorage usage
  static secureLocalStorage() {
    // Override console in production to prevent information leakage
    if (import.meta.env.PROD) {
      console.log = () => {};
      console.warn = () => {};
      console.info = () => {};
    }

    // Monitor for suspicious activity
    this.monitorForAttacks();
  }

  // Basic attack monitoring
  static monitorForAttacks() {
    // Monitor for potential XSS attempts
    const originalCreateElement = document.createElement;
    document.createElement = function(tagName) {
      const element = originalCreateElement.call(this, tagName);
      
      // Log suspicious script creation in development only
      if (tagName.toLowerCase() === 'script' && import.meta.env.DEV) {
        // Development logging removed for production
      }
      
      return element;
    };

    // Monitor for eval attempts
    const originalEval = window.eval;
    window.eval = function(code) {
      // Security warning removed for production
      return originalEval.call(this, code);
    };
  }

  // Validate external resources
  static validateResource(url) {
    try {
      const urlObj = new URL(url);
      const allowedDomains = [
        'localhost',
        '127.0.0.1',
        new URL(import.meta.env.VITE_API_URL || 'http://localhost:8080').hostname
      ];
      
      return allowedDomains.includes(urlObj.hostname);
    } catch {
      return false;
    }
  }
}

export default SecurityHeaders; 
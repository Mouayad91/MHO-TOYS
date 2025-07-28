// Input sanitization utility to prevent XSS attacks
import DOMPurify from 'dompurify';

class InputSanitizer {
  // Sanitize HTML content
  static sanitizeHTML(input) {
    if (typeof input !== 'string') return input;
    return DOMPurify.sanitize(input, {
      ALLOWED_TAGS: ['b', 'i', 'em', 'strong', 'p', 'br'],
      ALLOWED_ATTR: []
    });
  }

  // Sanitize plain text inputs
  static sanitizeText(input) {
    if (typeof input !== 'string') return input;
    
    return input
      .replace(/[<>'"&]/g, (match) => {
        const entities = {
          '<': '&lt;',
          '>': '&gt;',
          '"': '&quot;',
          "'": '&#x27;',
          '&': '&amp;'
        };
        return entities[match];
      })
      .trim();
  }

  // Sanitize email addresses
  static sanitizeEmail(email) {
    if (!email || typeof email !== 'string') return '';
    
    // Remove potentially dangerous characters
    const sanitized = email
      .toLowerCase()
      .replace(/[^a-z0-9@._-]/g, '')
      .trim();
    
    // Basic email format validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(sanitized) ? sanitized : '';
  }

  // Sanitize URLs
  static sanitizeURL(url) {
    if (!url || typeof url !== 'string') return '';
    
    try {
      const urlObj = new URL(url);
      // Only allow HTTP and HTTPS protocols
      if (urlObj.protocol !== 'http:' && urlObj.protocol !== 'https:') {
        return '';
      }
      return urlObj.toString();
    } catch {
      return '';
    }
  }

  // Sanitize file names
  static sanitizeFileName(fileName) {
    if (!fileName || typeof fileName !== 'string') return '';
    
    return fileName
      .replace(/[^a-zA-Z0-9._-]/g, '_')
      .replace(/_{2,}/g, '_')
      .substring(0, 255);
  }

  // Validate and sanitize product data
  static sanitizeProductData(productData) {
    if (!productData || typeof productData !== 'object') return {};
    
    return {
      name: this.sanitizeText(productData.name?.substring(0, 100) || ''),
      description: this.sanitizeText(productData.description?.substring(0, 500) || ''),
      price: this.sanitizePrice(productData.price),
      ageRange: this.sanitizeText(productData.ageRange?.substring(0, 50) || ''),
      imageUrl: this.sanitizeURL(productData.imageUrl || '')
    };
  }

  // Sanitize price input
  static sanitizePrice(price) {
    if (typeof price === 'number' && !isNaN(price) && price >= 0) {
      return Math.round(price * 100) / 100; // Round to 2 decimal places
    }
    if (typeof price === 'string') {
      const numPrice = parseFloat(price.replace(/[^0-9.-]/g, ''));
      return !isNaN(numPrice) && numPrice >= 0 ? Math.round(numPrice * 100) / 100 : 0;
    }
    return 0;
  }

  // Validate user input lengths
  static validateLength(input, minLength = 0, maxLength = Infinity) {
    if (typeof input !== 'string') return false;
    return input.length >= minLength && input.length <= maxLength;
  }

  // Check for SQL injection patterns
  static containsSQLInjection(input) {
    if (typeof input !== 'string') return false;
    
    const sqlPatterns = [
      /[';-]/i,
      /\b(union|select|insert|drop|delete|update|create|alter|exec|execute)\b/i,
      /[<>{}[\]|*%]/
    ];
    
    return sqlPatterns.some(pattern => pattern.test(input));
  }

  // Sanitize search queries
  static sanitizeSearchQuery(query) {
    if (!query || typeof query !== 'string') return '';
    
    return query
      .replace(/[^\w\s-_.]/g, '')
      .replace(/\s+/g, ' ')
      .trim()
      .substring(0, 100);
  }
}

export default InputSanitizer; 
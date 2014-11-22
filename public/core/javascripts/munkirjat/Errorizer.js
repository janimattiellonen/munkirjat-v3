/**
 * @param jQuery object $containerElement
 * @param string elementClass
 * @constructor
 */
Munkirjat.Errorizer = function($containerElement, elementClass) {
    this.$containerElement  = $containerElement;
    this.elementClass       = elementClass;
};

Munkirjat.Errorizer.prototype = (function()
{
    return {
        errorize: function(messages) {
            this.clear();
            var self = this;
            _.each(messages, function(values, key) {
                self.errorizeElement(key, values);
            })
        },

        errorizeElement: function(targetName, value) {
            $(this.$containerElement).addClass('errorized');
            $errorDiv = $('<div class="col-sm-offset-3 col-sm-9"></div>').addClass('errorized-message');
            $ul = $('<ul></ul>');
            $ul.append($('<li></li>').text(value.message));

            $errorDiv.html($ul);
            	
            $item = $('*[name="' + value.key + '"]', this.$containerElement).closest("." + this.elementClass);
            $item.append($errorDiv);
        },

        clear: function() {
            $(this.$containerElement).find('.errorized-message').remove();
        }
    }
})();
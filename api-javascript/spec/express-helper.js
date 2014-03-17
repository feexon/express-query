beforeEach(function () {
    window.origin=window.origin||$
    $= window.origin.extend(window.origin)

    jasmine.addMatchers({
        isA: function () {
            return {
                compare: function (actual, expected) {
                    return {
                        message: "Expected isA <" + expected.name + ">, but was <" + actual.constructor.name + ">",
                        pass: actual.constructor == expected
                    }
                }
            };
        },
        hasClass: function () {
            return {
                compare: function (actual, expected) {
                    return {
                        message: "Expected element containing className:"+expected+",but was:"+actual.get(0).className,
                        pass: actual.hasClass(expected)
                    }
                }
            };
        }

    });
});

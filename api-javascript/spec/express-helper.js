beforeEach(function () {
    jasmine.addMatchers({
        isA: function () {
            return {
                compare: function (actual, expected) {
                    return {
                        message:"Expected isA <"+expected.name+">, but was <"+actual.constructor.name+">",
                        pass: actual.constructor == expected
                    }
                }
            };
        }
    });
});

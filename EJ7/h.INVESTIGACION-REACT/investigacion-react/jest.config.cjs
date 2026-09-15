module.exports = {
    testEnvironment: "jsdom",
    setupFilesAfterEnv: ["<rootDir>/jest.setup.cjs"],
    transform: {
        "^.+\\.[jt]sx?$": "babel-jest"
    }
};
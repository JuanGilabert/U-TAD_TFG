
export async function checkIfUserIsAuthorizedFunction(id, userId, collectionNameToSearch = "") {
    const db = await connectDB();
    try {
        const isAuthorized = await db.collection(collectionNameToSearch).findOne({ userId: userId, _id: id });
        return isAuthorized === null ? false : true;
    } catch (error) {
        console.error(error);
        return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
    }
}